package bazis.utils.global_person_search;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.ext.ReportData;
import bazis.utils.global_person_search.json.JsonAsText;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.Jsonable;
import bazis.utils.global_person_search.protocol.CompositeProtocol;
import bazis.utils.global_person_search.protocol.ForkProtocol;
import bazis.utils.global_person_search.protocol.JspProtocol;
import bazis.utils.global_person_search.protocol.RtfProtocol;
import bazis.utils.global_person_search.sx.DownloadUrl;
import bazis.utils.global_person_search.sx.MspMap;
import bazis.utils.global_person_search.sx.SxPerson;
import bazis.utils.global_person_search.sx.SxReport;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import sx.admin.AdmAction;
import sx.admin.AdmApplication;
import sx.admin.AdmRequest;
import sx.common.DateUtils;
import sx.datastore.SXId;

public abstract class BaseSearchUtil extends AdmAction {

    private final String url;

    private final Func<Person, Jsonable> requests;

    protected BaseSearchUtil(String url) {
        this(
            url,
            new Func<Person, Jsonable>() {
                @Override
                public Jsonable apply(Person person) throws BazisException {
                    return new JsonRequest(new JsonObject())
                        .withSnils(person.snils());
                }
            }
        );
    }

    protected BaseSearchUtil(String url, Func<Person, Jsonable> requests) {
        super();
        this.url = url;
        this.requests = requests;
    }

    @Override
    public final void execute(
        final AdmRequest request, final AdmApplication app) throws Exception {
        final String cmd = request.getParam("cmd");
        if (cmd == null) super.includeTemplate(
            "global_person_search/openwindow", request
        );
        else if (cmd.equals("openWindowCmd")) super.includeTemplate(
            "global_person_search/params", request
        );
        else if (cmd.equals("paramsCmd")) {
            final Person person = new SxPerson(
                new SXId(request.getAction().getObjId())
            );
            final List<String> errors = new LinkedList<>();
//            final Iterable<Person> persons =
//                new IterableOf<Person>(
//                    new FakePerson(
//                        "Иванов Владислав Александрович",
//                        new FakeAppoint(
//                            "Первое назначение",
//                            "2019-01-01", "2019-12-31"
//                        ),
//                        new FakeAppoint(
//                            "Второе назначение",
//                            "2020-01-01", "2020-12-31"
//                        )
//                    )
//                );
            final Iterable<Person> persons =
                new JsonPersons(
                    new JsonParser().parse(
                        new Server(this.url, errors).send(
                            new JsonAsText(
                                this.requests.apply(person)
                            ).asString()
                        )
                    ).getAsJsonArray()
                );
            if (new IsEmpty(persons).value()) errors.add(
                "Нет информации о данном гражданине на других базах"
            );
            if (!errors.isEmpty()) request.set("error", errors.get(0));
            final Report report =
                new SxReport("globalPersonSearchProtocol");
            final Protocol protocol = new ForkProtocol(
                person.fio(), person.birthdate(),
                new CompositeProtocol(
                    new JspProtocol(request, "completely"),
                    new RtfProtocol(report, 1)
                ),
                new CompositeProtocol(
                    new JspProtocol(request, "partially"),
                    new RtfProtocol(report, 2)
                )
            );
            final Date
                start = new DefaultDateFormat().parse(
                    String.format(
                        "%s-%s-01",
                        request.getParam("yearOfStart"),
                        request.getParam("monthOfStart")
                    )
                ),
                end = DateUtils.getMonthYearEndDate(
                    new DefaultDateFormat().parse(
                        String.format(
                            "%s-%s-01",
                            request.getParam("yearOfEnd"),
                            request.getParam("monthOfEnd")
                        )
                    )
                );
            final Map<String, String> msp =
                "on".equals(request.getParam("isAllMsp"))
                    ? new EmptyMap<String, String>()
                    : new MspMap(request.getParam("data(mspList)"));
            for (
                final Person prs : new MappedIterable<>(
                    persons,
                    new Func<Person, Person>() {
                        @Override
                        public Person apply(Person origin) {
                            return new RequestedPerson(
                                origin, msp.keySet(), start, end
                            );
                        }
                    }
                )
            ) protocol.write(prs);
            request.set(
                "protocol",
                new DownloadUrl(
                    report.create(
                        new ReportData.Immutable()
                            .withDate("startDate", start)
                            .withDate("endDate", end)
                            .withString(
                                "mspList",
                                new JoinedText(
                                    ", ", msp.values()
                                ).asString()
                            )
                    )
                ).asString()
            );
            super.includeTemplate(
                "global_person_search/result", request
            );
        }
        else throw new BazisException("Unknown cmd");
    }

}
