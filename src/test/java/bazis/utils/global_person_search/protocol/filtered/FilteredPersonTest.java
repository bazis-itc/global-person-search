package bazis.utils.global_person_search.protocol.filtered;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.fake.FakeAppoint;
import bazis.utils.global_person_search.fake.FakePayout;
import bazis.utils.global_person_search.fake.FakePerson;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import sx.common.MonthYearBean;

public final class FilteredPersonTest {

    @Test
    public void freshAppoint() throws BazisException {
        MatcherAssert.assertThat(
            new FilteredPerson(
                new FakePerson(
                    "",
                    new FakeAppoint()
                        .withDates("2021-02-01", "2021-02-15")
                        .withPayouts(new EmptyIterable<Payout>())
                ),
                new EmptyIterable<String>(),
                new MonthYearBean(2021, 1, 1).getDate(),
                new MonthYearBean(2021, 12, 31).getDate()
            ).appoints(),
            Matchers.not(Matchers.emptyIterable())
        );
    }

    @Test
    public void staleAppoint() throws BazisException {
        MatcherAssert.assertThat(
            new FilteredPerson(
                new FakePerson(
                    "",
                    new FakeAppoint()
                        .withDates("2020-01-01", "2020-12-31")
                        .withPayouts(
                            new IterableOf<Payout>(
                                new FakePayout(2020, 1)
                            )
                        )
                ),
                new EmptyIterable<String>(),
                new MonthYearBean(2021, 1, 1).getDate(),
                new MonthYearBean(2021, 12, 31).getDate()
            ).appoints(),
            Matchers.emptyIterable()
        );
    }

    @Test
    public void staleAppointWithFreshPayout() throws BazisException {
        MatcherAssert.assertThat(
            new FilteredPerson(
                new FakePerson(
                    "",
                    new FakeAppoint()
                        .withDates("2020-01-01", "2020-12-31")
                        .withPayouts(
                            new IterableOf<Payout>(
                                new FakePayout(2021, 1)
                            )
                        )
                ),
                new EmptyIterable<String>(),
                new MonthYearBean(2021, 1, 1).getDate(),
                new MonthYearBean(2021, 12, 31).getDate()
            ).appoints(),
            Matchers.not(Matchers.emptyIterable())
        );
    }

}