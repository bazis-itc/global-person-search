package bazis.utils.global_person_search.action;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.json.Jsonable;
import bazis.utils.global_person_search.misc.Config;
import bazis.utils.global_person_search.misc.ParamsOf;
import bazis.utils.global_person_search.misc.RequestPerson;
import bazis.utils.global_person_search.misc.Server;
import bazis.utils.global_person_search.protocol.CompoundProtocol;
import bazis.utils.global_person_search.protocol.FilteredProtocol;
import bazis.utils.global_person_search.protocol.JspProtocol;
import bazis.utils.global_person_search.protocol.SplitProtocol;
import bazis.utils.global_person_search.protocol.rtf.ZipProtocol;
import com.google.gson.JsonObject;
import java.util.Map;
import sx.admin.AdmRequest;
import sx.cms.CmsApplication;

@SuppressWarnings("OverlyCoupledClass")
public final class ResultAction implements SitexAction {

    public static final String NO_RESULT =
        "Нет информации о данном гражданине на других базах";

    private final Esrn esrn;

    private final Map<String, String> config;

    public ResultAction(Esrn esrn) {
        this.esrn = esrn;
        this.config = new Config();
    }

    @Override
    @SuppressWarnings("OverlyCoupledMethod")
    public void execute(AdmRequest request) throws BazisException {
        final Opt<Number> personId = new ParamsOf(request).personId();
        final Person person = personId.has()
            ? this.esrn.person(personId.get())
            : new RequestPerson(request);
        final Server server = new Server(this.centralUrl());
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
                    new IsEmpty(persons).value()
                        ? ResultAction.NO_RESULT : "",
                    server.fails().isEmpty()
                        ? "" : "Не опрошены районы: " + server.fails()
                ).trim()
            );
        new FilteredProtocol(
            new SplitProtocol(
                new CompoundProtocol(
                    new JspProtocol(), new ZipProtocol(this.esrn)
                ),
                person
            ),
            this.esrn.measures(new ParamsOf(request).msp()).keySet(),
            new ParamsOf(request).startDate(),
            new ParamsOf(request).endDate()
        ).append(persons).outputTo(request);
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

    private String centralUrl() throws BazisException {
        final String result;
        final String fromConfig = this.config.get("centralUrl");
        if (fromConfig.isEmpty()) {
            result = CmsApplication.getCmsApplication()
                .getString("regCentralServerURL");
            if (result == null || result.isEmpty()) throw new BazisException(
                "В настройках соц. регистра не указан адрес центральной базы"
            );
        } else result = fromConfig;
        return result;
    }

}
