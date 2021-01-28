package bazis.utils.global_person_search.json;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.fake.FakePerson;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonPersonsTest {

    @Test
    public void test() throws BazisException {
        final List<String> surnames =
            new ListOf<>("Иванов", "Петров", "Сидоров");
        final List<Person> persons = new ListOf<>(
            new JsonPersons(
                new JsonPersons(
                    new MappedIterable<>(
                        surnames,
                        new Func<String, Person>() {
                            @Override
                            public Person apply(String fio) {
                                return new FakePerson(fio);
                            }
                        }
                    )
                ).asJson()
            )
        );
        MatcherAssert.assertThat(
            "size", persons, Matchers.hasSize(surnames.size())
        );
        //noinspection ForLoopReplaceableByWhile
        for (int idx = 0; idx < surnames.size(); idx++)
            MatcherAssert.assertThat(
                "item",
                persons.get(idx).fio(),
                Matchers.equalTo(surnames.get(idx))
            );
    }

}