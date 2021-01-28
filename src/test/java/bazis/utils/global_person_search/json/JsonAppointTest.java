package bazis.utils.global_person_search.json;

import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Period;
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
            "test periods",
            converted.periods(),
            Matchers.<Period>iterableWithSize(
                new ListOf<>(origin.periods()).size()
            )
        );
        MatcherAssert.assertThat(
            "test payouts",
            converted.payouts(),
            Matchers.<Payout>iterableWithSize(
                new ListOf<>(origin.payouts()).size()
            )
        );
    }

}