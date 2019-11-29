package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.iterable.IterableOf;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.fake.FakePerson;
import bazis.utils.global_person_search.fake.FakeProtocol;
import java.util.Collection;
import java.util.LinkedList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class SplitProtocolTest {

    @Test
    public void test() throws Exception {
        final Person origin = new FakePerson();
        final Collection<String> output = new LinkedList<>();
        new SplitProtocol(new FakeProtocol(output), origin)
            .append(
                new IterableOf<>(
                    new FakePerson("Иванов Дмитрий Александрович"),
                    origin,
                    new FakePerson(origin.fio(), new IsoDate("1983-10-28"))
                )
            );
        MatcherAssert.assertThat(
            output,
            Matchers.contains(
                "Иванов Владислав Александрович 27.09.1990",
                new ConcatedText(
                    "Иванов Дмитрий Александрович 27.09.1990, ",
                    "Иванов Владислав Александрович 28.10.1983"
                ).asString()
            )
        );
    }

}