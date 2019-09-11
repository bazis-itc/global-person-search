package bazis.utils;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Server;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.jsp.JspPerson;
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
        final String cmd = request.getParam("cmd");
        if (cmd == null) super.includeTemplate(
            "global_person_search/openwindow", request
        );
        else if (cmd.equals("openWindowCmd")) {
            final Iterable<Person> persons =
                new JsonPersons(
                    new JsonParser().parse(
                        new Server(
                            "http://10.65.12.11:8080/update_test_central/"
                        ).send(
                            new SXId(request.getAction().getObjId())
                                .getObj(SNILS_ATTR)
                                .getStringAttr(SNILS_ATTR)
                        )
                    ).getAsJsonArray()
                );
            if (new IsEmpty(persons).value()) request.set(
                "error",
                "Нет информации о данном гражданине на других базах"
            );
            else request.set(
                "persons",
                new ListOf<>(
                    new MappedIterable<>(
                        persons,
                        new Func<Person, JspPerson>() {
                            @Override
                            public JspPerson apply(Person person) {
                                return new JspPerson(person);
                            }
                        }
                    )
                ).toArray(new JspPerson[0])
            );
//            request.set(
//                "persons",
//                new JspPerson[] {
//                    new JspPerson(new FakePerson()),
//                    new JspPerson(new FakePerson())
//                }
//            );
            super.includeTemplate(
                "global_person_search/result", request
            );
        }
        else throw new BazisException("Unknown cmd");
    }

}
