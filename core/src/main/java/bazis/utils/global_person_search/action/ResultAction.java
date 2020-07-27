package bazis.utils.global_person_search.action;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.ParamsOf;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.PropertiesOf;
import bazis.utils.global_person_search.RequestPerson;
import bazis.utils.global_person_search.RequestedPersons;
import bazis.utils.global_person_search.Server;
import bazis.utils.global_person_search.dates.FormattedDate;
import bazis.utils.global_person_search.ext.ReportData;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.json.Jsonable;
import bazis.utils.global_person_search.protocol.CompoundProtocol;
import bazis.utils.global_person_search.protocol.RtfProtocol;
import bazis.utils.global_person_search.protocol.SplitProtocol;
import bazis.utils.global_person_search.protocol.jsp.JspProtocol;
import bazis.utils.global_person_search.sx.SxReport;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.Map;
import sx.admin.AdmRequest;

@SuppressWarnings("OverlyCoupledClass")
public final class ResultAction implements SitexAction {

    private static final String NO_RESULT =
        "Нет информации о данном гражданине на других базах";

    private final Esrn esrn;

    private final Map<String, String> config;

    public ResultAction(Esrn esrn) {
        this.esrn = esrn;
        this.config = new PropertiesOf(
            this.getClass(), "ResultAction.properties"
        );
    }

    @Override
    @SuppressWarnings("OverlyCoupledMethod")
    public void execute(AdmRequest request) throws BazisException {
        final Opt<Number> personId = new ParamsOf(request).personId();
        final Person person = personId.has()
            ? this.esrn.person(personId.get())
            : new RequestPerson(request);
        final Server server = new Server(this.config.get("centralUrl"));
        final String response = server.send(
            new JsonText(this.makeRequest(person)).asString()
        );
        final Iterable<Person> persons =
            new JsonPersons(new JsonText(response).asJson());
        request.set("persons", response);
        request.set("fails", server.fails());
        request.set(
            "canCreateDoc",
            Boolean.parseBoolean(this.config.get("canCreateDoc"))
                && personId.has()
        );
        if (new IsEmpty(persons).value() || !server.fails().isEmpty())
            request.set(
                "error", String.format(
                    "%s %s",
                    ResultAction.NO_RESULT,
                    server.fails().isEmpty()
                        ? "" : "Не опрошены районы: " + server.fails()
                ).trim()
            );
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

    private Jsonable makeRequest(Person person) throws BazisException {
        final JsonRequest base = new JsonRequest(new JsonObject())
            .withSnils(person.snils());
        return Boolean.parseBoolean(this.config.get("extendedSearch"))
            ? base
                .withFio(person.fio())
                .withBirthdate(person.birthdate())
            : base;
    }

}
