package bazis.utils.global_person_search.jdbc;

import bazis.utils.global_person_search.Payout;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JdbcPayoutsTest {

    @Test
    public void threeElements() {
        MatcherAssert.assertThat(
            new JdbcPayouts("1|2|3|"),
            Matchers.<Payout>iterableWithSize(3)
        );
    }

    @Test
    public void zeroElements() {
        MatcherAssert.assertThat(
            new JdbcPayouts(""),
            Matchers.emptyIterable()
        );
    }

}