package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.utils.global_person_search.action.CreateDoc;
import bazis.utils.global_person_search.fake.FakeAppoint;
import bazis.utils.global_person_search.fake.FakePerson;
import bazis.utils.global_person_search.fake.FakeRequest;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonText;
import java.util.Collections;
import java.util.Map;
import org.jooq.impl.DSL;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public final class CreateDocITCase {

    @Test
    public void test() throws BazisException {
        new CreateDoc(
            DSL.using(
                "jdbc:sqlserver://sqlbazis.bazis.local;databaseName=ivanov_mord_kadosh",
                "ivanov", "BF64DVer"
            ),
            new Esrn() {
                @Override
                public Person person(Number id) {
                    return new FakePerson();
                }
                @Override
                public Map<String, String> measures(String links) {
                    return Collections.singletonMap(
                        "37145780-704c-48a2-9272-1f99afddaa9f",
                        "Ежемесячная денежная компенсация военнослужащим"
                    );
                }
            }
        ).execute(
            new FakeRequest(
                new MapOf<>(
                    new Entry<>("objId", "13198@wmPersonalCard"),
                    new Entry<>("yearOfStart", "2019"),
                    new Entry<>("monthOfStart", "1"),
                    new Entry<>("yearOfEnd", "2020"),
                    new Entry<>("monthOfEnd", "2"),
                    new Entry<>("isAllMsp", "on"),
                    new Entry<>("data(mspList)", ""),
                    new Entry<>("fails", "Неопрошенный район"),
                    new Entry<>(
                        "persons",
                        new JsonText(
                            new JsonPersons(
                                new IterableOf<Person>(
                                    new FakePerson(),
                                    new FakePerson(
                                        "Неиванов Владислав Александрович",
                                        new FakeAppoint()
                                    )
                                )
                            )
                        ).asString()
                    )
                )
            )
        );
    }

}