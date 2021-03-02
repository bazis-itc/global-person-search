package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.fake.FakeAppoint;
import bazis.utils.global_person_search.fake.FakeEsrn;
import bazis.utils.global_person_search.fake.FakePerson;
import bazis.utils.global_person_search.fake.FakeReport;
import bazis.utils.global_person_search.fake.FakeRequest;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import sx.common.reportsystem.MockReport;

public final class SimpleProtocolTest {

    @Test
    public void test() throws Exception {
        final Map<String, Object> output = new HashMap<>(0);
        final FakeReport report = new FakeReport(1, output);
        new SimpleProtocol(new FakeEsrn(report)).append(
            new IterableOf<Person>(
                new FakePerson("Иванов Сидр Петрович", new FakeAppoint())
            )
        ).outputTo(new FakeRequest());
        MatcherAssert.assertThat(
            output,
            Matchers.allOf(
                Matchers.<String, Object>hasEntry(
                    "period", "с 01.01.2019 по 31.12.2019"
                ),
                Matchers.<String, Object>hasEntry(
                    "person",
                    new ConcatedText(
                        "Иванов Сидр Петрович, ",
                        "27.09.1990, г. Саранск, ул. Лодыгина, д. 3"
                    ).asString()
                )
            )
        );
    }

    @Ignore @Test
    public void itCase() throws BazisException {
        final Iterable<Person> persons = new IterableOf<Person>(
            new FakePerson(),
            new FakePerson(
                "Пустой Иван Иванович",
                new FakeAppoint().withPayouts(new EmptyIterable<Payout>())
            )
        );
        new SimpleProtocol(new FakeEsrn(new MockReport("/vrn.rtf")))
            .append(persons).append(persons).outputTo(new FakeRequest());
    }

}