package bazis.utils.global_person_search.date;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import java.util.Date;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class PeriodTest {

    @Test
    public void twoDates() throws BazisException {
        MatcherAssert.assertThat(
            new Period(
                " ",
                new IsoDate("2019-01-02").value(),
                new IsoDate("2019-12-01").value()
            ).asString(),
            Matchers.equalTo("с 02.01.2019 по 01.12.2019")
        );
    }

    @Test
    public void withoutStart() throws BazisException {
        MatcherAssert.assertThat(
            new Period(
                "unused",
                new EmptyOpt<Date>(),
                new OptOf<>(new IsoDate("2019-12-02").value())
            ).asString(),
            Matchers.equalTo("по 02.12.2019")
        );
    }

    @Test
    public void withoutEnd() throws BazisException {
        MatcherAssert.assertThat(
            new Period(
                " ",
                new OptOf<>(new IsoDate("2019-12-03").value()),
                new EmptyOpt<Date>()
            ).asString(),
            Matchers.equalTo("с 03.12.2019")
        );
    }

    @Test
    public void withoutAnything() throws BazisException {
        MatcherAssert.assertThat(
            new Period(
                " ", new EmptyOpt<Date>(), new EmptyOpt<Date>()
            ).asString(),
            Matchers.isEmptyString()
        );
    }

}