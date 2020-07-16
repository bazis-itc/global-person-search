package bazis.utils.global_person_search;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.dates.FormattedDate;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.CheckedFunc;
import bazis.utils.global_person_search.ext.ReportData;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.json.Jsonable;
import bazis.utils.global_person_search.protocol.CompoundProtocol;
import bazis.utils.global_person_search.protocol.RtfProtocol;
import bazis.utils.global_person_search.protocol.SplitProtocol;
import bazis.utils.global_person_search.protocol.jsp.JspProtocol;
import bazis.utils.global_person_search.sx.MspMap;
import bazis.utils.global_person_search.sx.SxPerson;
import bazis.utils.global_person_search.sx.SxReport;
import java.util.Date;
import java.util.Map;
import sx.admin.AdmRequest;
import sx.common.DateUtils;
import sx.datastore.SXId;

@SuppressWarnings("OverlyCoupledClass")
public final class ResultAction implements SitexAction {

    private static final String NO_RESULT =
        "Нет информации о данном гражданине на других базах";

    private final Text url;

    private final Func<Person, Jsonable> requests;

    ResultAction(Text url, Func<Person, Jsonable> requests) {
        this.url = url;
        this.requests = requests;
    }

    @Override
    @SuppressWarnings("OverlyCoupledMethod")
    public void execute(AdmRequest request) throws BazisException {
        final Person person = new SxPerson(
            new SXId(request.getAction().getObjId())
        );
        final Iterable<Person> persons =
            new JsonPersons(
                new JsonText(
                    new Server(new CheckedText(this.url).asString()).send(
                        new JsonText(
                            new CheckedFunc<>(this.requests).apply(person)
                        ).asString()
                    )
                ).asJson()
            );
        if (new IsEmpty(persons).value())
            request.set("error", ResultAction.NO_RESULT);
        final Date
            start = this.dateFrom(request, "yearOfStart", "monthOfStart"),
            end = DateUtils.getMonthYearEndDate(
                this.dateFrom(request, "yearOfEnd", "monthOfEnd")
            );
        final Map<String, String> msp =
            "on".equals(request.getParam("isAllMsp"))
                ? new EmptyMap<String, String>()
                : new MspMap(request.getParam("data(mspList)"));
        new SplitProtocol(
            new CompoundProtocol(
                new JspProtocol(),
                new RtfProtocol(new SxReport("globalPersonSearchProtocol"))
            ),
            person
        )
            .append(new RequestedPersons(persons, msp.keySet(), start, end))
            .outputTo(
                request,
                new ReportData.Immutable()
                    .withString(
                        "currentDate",
                        new FormattedDate("dd.MM.yyyy HH:mm:ss", new Date())
                    )
                    .withDate("startDate", start)
                    .withDate("endDate", end)
                    .withString(
                        "mspList",
                        new JoinedText(", ", msp.values())
                    )
                    .withString(
                        "message",
                        new IsEmpty(persons).value()
                            ? ResultAction.NO_RESULT : ""
                    )
            );
    }

    @SuppressWarnings("MethodMayBeStatic")
    private Date dateFrom(AdmRequest request, String year, String month)
        throws BazisException {
        return new IsoDate(
            String.format(
                "%s-%s-01", request.getParam(year), request.getParam(month)
            )
        ).value();
    }

}
