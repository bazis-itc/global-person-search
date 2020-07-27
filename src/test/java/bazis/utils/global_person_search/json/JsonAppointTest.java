package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.fake.FakeAppoint;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonAppointTest {

    @Test
    public void canPrintAndParse() throws BazisException {
        final Appoint
            origin = new FakeAppoint(),
            converted = new JsonAppoint(
                new JsonText(
                    new JsonText(new JsonAppoint(origin)).asString()
                ).asJson()
            );
        MatcherAssert.assertThat(
            "test type",
            converted.type(), Matchers.equalTo(origin.type())
        );
        MatcherAssert.assertThat(
            "test msp",
            converted.msp(), Matchers.equalTo(origin.msp())
        );
        MatcherAssert.assertThat(
            "test category",
            converted.category(), Matchers.equalTo(origin.category())
        );
        MatcherAssert.assertThat(
            "test child",
            converted.child(), Matchers.equalTo(origin.child())
        );
        MatcherAssert.assertThat(
            "test status",
            converted.status(), Matchers.equalTo(origin.status())
        );
        MatcherAssert.assertThat(
            "test startDate",
            new IsoDate(converted.startDate().get()).asString(),
            Matchers.equalTo(
                new IsoDate(origin.startDate().get()).asString()
            )
        );
        MatcherAssert.assertThat(
            "test endDate",
            new IsoDate(converted.endDate().get()).asString(),
            Matchers.equalTo(
                new IsoDate(origin.endDate().get()).asString()
            )
        );
        MatcherAssert.assertThat(
            "test payouts",
            converted.payouts(), Matchers.<Payout>iterableWithSize(3)
        );
    }

    @Test
    public void emptyDates() throws BazisException {
        final Appoint
            origin = new FakeAppoint().withDates("", ""),
            converted = new JsonAppoint(
                new JsonText(
                    new JsonText(new JsonAppoint(origin)).asString()
                ).asJson()
            );
        MatcherAssert.assertThat(
            "start date not empty",
            converted.startDate().has(), Matchers.is(false)
        );
        MatcherAssert.assertThat(
            "end date not empty",
            converted.endDate().has(), Matchers.is(false)
        );
    }

}