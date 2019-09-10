package bazis.utils;

import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Server;
import bazis.utils.global_person_search.json.JsonPersons;
import com.google.gson.JsonParser;
import sx.admin.AdmAction;
import sx.admin.AdmApplication;
import sx.admin.AdmRequest;
import sx.datastore.SXId;

//bazis.utils.GlobalPersonSearchUtil
public final class GlobalPersonSearchUtil extends AdmAction {

    private static final String SNILS_ATTR = "snils";

    @Override
    public void execute(
        AdmRequest request, AdmApplication app) throws Exception {
        final Iterable<Person> persons = new JsonPersons(
            new JsonParser().parse(
                new Server("http://10.65.12.11:8080/update_test_central/")
                    .send(
                        new SXId(request.getAction().getObjId())
                            .getObj(SNILS_ATTR).getStringAttr(SNILS_ATTR)
                    )
            ).getAsJsonArray()
        );
        for (final Person person : persons) {
            request.getAction().addMessage(person.fio());
            request.getAction().addMessage(person.birthdate().toString());
            request.getAction().addMessage(person.address());
        }
        super.includeTemplate("defaultreload", request);
    }

}
