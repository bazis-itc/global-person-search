package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.fake.FakeAppoint;
import bazis.utils.global_person_search.fake.FakePerson;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonPersonTest {

    @SuppressWarnings("SpellCheckingInspection")
    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void canPrintAndParse() throws BazisException {
        final Person
            origin = new FakePerson(
                "Путин Владимир Владимирович",
                new FakeAppoint(), new FakeAppoint(), new FakeAppoint()
            ),
            converted = new JsonPerson(new JsonPerson(origin).asJson());
        MatcherAssert.assertThat(
            "test fio",
            converted.fio(), Matchers.equalTo(origin.fio())
        );
        MatcherAssert.assertThat(
            "test birthdate",
            JsonPersonTest.DATE_FORMAT.format(converted.birthdate()),
            Matchers.equalTo(
                JsonPersonTest.DATE_FORMAT.format(origin.birthdate())
            )
        );
        MatcherAssert.assertThat(
            "test address",
            converted.address(), Matchers.equalTo(origin.address())
        );
        MatcherAssert.assertThat(
            "test snils",
            converted.snils(), Matchers.equalTo(origin.snils())
        );
        MatcherAssert.assertThat(
            "test borough",
            converted.borough(), Matchers.equalTo(origin.borough())
        );
        MatcherAssert.assertThat(
            "test passport",
            converted.passport(), Matchers.equalTo(origin.passport())
        );
        MatcherAssert.assertThat(
            "test appoints",
            converted.appoints(), Matchers.<Appoint>iterableWithSize(3)
        );
    }

}