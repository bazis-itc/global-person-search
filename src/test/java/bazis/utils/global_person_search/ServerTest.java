package bazis.utils.global_person_search;

import java.util.Collection;
import java.util.LinkedList;

public final class ServerTest {

    public static void main(String... args) throws Exception {
        final Collection<String> log = new LinkedList<>();
        final String response = new Server(
            "http://10.65.12.11:8080/update_test_central/", log
        ).send("04859235392");
        System.out.println(log);
        System.out.println(response);
//        final Iterable<Person> persons = new JsonPersons(
//            new JsonParser().parse(response).getAsJsonArray()
//        );
//        for (final Person person : persons) {
//            System.out.println(person.fio());
//            System.out.println(person.birthdate());
//            System.out.println(person.address());
//        }
    }

}
