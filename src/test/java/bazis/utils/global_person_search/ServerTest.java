package bazis.utils.global_person_search;

public final class ServerTest {

    public static void main(String... args) throws Exception {
        final String response =
            new Server("http://10.65.12.11:8080/update_test_central/")
                .send("04859235392");
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
