package bazis.utils.global_person_search;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.dates.FormattedDate;
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
import bazis.utils.global_person_search.sx.SxReport;
import java.util.Date;
import java.util.Map;
import sx.admin.AdmRequest;

@SuppressWarnings("OverlyCoupledClass")
public final class ResultAction implements SitexAction {

    private static final String NO_RESULT =
        "Нет информации о данном гражданине на других базах";

    private final String url;

    private final Func<Person, Jsonable> requests;

    private final Esrn esrn;

    ResultAction(String url,
        Func<Person, Jsonable> requests, Esrn esrn) {
        this.url = url;
        this.requests = requests;
        this.esrn = esrn;
    }

    @Override
    @SuppressWarnings("OverlyCoupledMethod")
    public void execute(AdmRequest request) throws BazisException {
        final Person person = this.esrn.person(new ParamsOf(request).objId());
        final Server server = new Server(this.url);
        final String response = server.send(
            new JsonText(
                new CheckedFunc<>(this.requests).apply(person)
            ).asString()
        );
        final Iterable<Person> persons =
            new JsonPersons(new JsonText(response).asJson());
        request.set("persons", response);
        request.set("fails", server.fails());
        if (new IsEmpty(persons).value())
            request.set("error", ResultAction.NO_RESULT);
        final Date
            start = new ParamsOf(request).startDate(),
            end = new ParamsOf(request).endDate();
        final Map<String, String> msp =
            this.esrn.measures(new ParamsOf(request).msp());
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
                    .withString("failures", server.fails())
                    .withString(
                        "message",
                        new IsEmpty(persons).value()
                            ? ResultAction.NO_RESULT : ""
                    )
            );
    }

}
