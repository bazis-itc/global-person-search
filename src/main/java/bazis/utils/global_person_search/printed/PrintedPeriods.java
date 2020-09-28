package bazis.utils.global_person_search.printed;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Period;

public final class PrintedPeriods implements Text {

    private final Iterable<Period> periods;

    public PrintedPeriods(Iterable<Period> periods) {
        this.periods = periods;
    }

    @Override
    public String asString() throws BazisException {
        return new CheckedText(
            new JoinedText(
                ", ",
                new MappedIterable<>(
                    this.periods,
                    new Func<Period, String>() {
                        @Override
                        public String apply(Period period)
                            throws BazisException {
                            return new PrintedPeriod(period).asString();
                        }
                    }
                )
            )
        ).asString();
    }

}
