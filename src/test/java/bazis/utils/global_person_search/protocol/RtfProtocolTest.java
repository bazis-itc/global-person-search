package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.map.EmptyMap;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Report;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.ext.HtmlReport;
import bazis.utils.global_person_search.fake.FakeAppoint;
import bazis.utils.global_person_search.fake.FakeEsrn;
import bazis.utils.global_person_search.fake.FakePerson;
import bazis.utils.global_person_search.fake.FakeReport;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

public final class RtfProtocolTest {

    @Test
    public void test() throws Exception {
        final Map<String, Object> output = new HashMap<>(0);
        new RtfProtocol(
            new FakeEsrn(),
            new FakeReport(1, output)
        ).append(
            new IterableOf<Person>(
                new FakePerson("Иванов Сидр Петрович", new FakeAppoint())
            )
        );
        //noinspection HardcodedLineSeparator
        MatcherAssert.assertThat(
            output,
            Matchers.allOf(
                Matchers.<String, Object>hasEntry(
                    "period", "с 01.01.2019\nпо 31.12.2019"
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
    public void test2() throws BazisException {
        final Report report =
            new HtmlReport(new File("D:\\Exchange\\report.html"));
        new RtfProtocol(new FakeEsrn(), report).append(
            new IterableOf<Person>(
                new FakePerson(),
                new FakePerson(
                    "Пустой Иван Иванович",
                    new FakeAppoint().withPayouts(new EmptyIterable<Payout>())
                )
            )
        );
        report.create(new EmptyMap<String, Object>());
    }

}