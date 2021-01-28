package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.fake.FakePayout;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonPayoutTest {

    @Test
    public void test() throws BazisException {
        final Payout
            origin = new FakePayout(),
            parsed = new JsonPayout(
                new JsonText(
                    new JsonText(new JsonPayout(origin)).asString()
                ).asJson()
            );
        MatcherAssert.assertThat(
            "test date",
            parsed.date(), Matchers.equalTo(origin.date())
        );
        MatcherAssert.assertThat(
            "test year",
            parsed.year(), Matchers.equalTo(origin.year())
        );
        MatcherAssert.assertThat(
            "test month",
            parsed.month(), Matchers.equalTo(origin.month())
        );
        MatcherAssert.assertThat(
            "test sum",
            parsed.sum(), Matchers.equalTo(origin.sum())
        );
    }

}