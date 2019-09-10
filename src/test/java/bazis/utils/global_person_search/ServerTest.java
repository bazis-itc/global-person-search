package bazis.utils.global_person_search;

import bazis.utils.global_person_search.json.JsonPersons;
import com.google.gson.JsonParser;

public final class ServerTest {

    public static void main(String... args) throws Exception {
        final Iterable<Person> persons = new JsonPersons(
            new JsonParser().parse(
                new Server("http://10.65.12.11:8080/update_test_central/")
                    .send("145-906-148 72")
            ).getAsJsonArray()
        );
        for (final Person person : persons) {
            System.out.println(person.fio());
            System.out.println(person.birthdate());
            System.out.println(person.address());
        }
    }

}
