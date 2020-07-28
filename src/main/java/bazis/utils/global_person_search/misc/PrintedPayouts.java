package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.ext.Sum;

public final class PrintedPayouts implements Text {

    private final Iterable<Payout> payouts;

    public PrintedPayouts(Iterable<Payout> payouts) {
        this.payouts = payouts;
    }

    @Override
    public String asString() throws BazisException {
        return new CheckedText(
            new JoinedText(
                "\n",
                new JoinedIterable<>(
                    new MappedIterable<>(
                        this.payouts,
                        new Func<Payout, String>() {
                            @Override
                            public String apply(Payout payout)
                                throws BazisException {
                                return new PrintedPayout(payout).asString();
                            }
                        }
                    ),
                    new IterableOf<>(
                        new IsEmpty(this.payouts).value() ? ""
                            : String.format(
                                "Итого: %.02f",
                                new Sum(
                                    new MappedIterable<>(
                                        this.payouts,
                                        new Func<Payout, Number>() {
                                            @Override
                                            public Number apply(Payout payout) {
                                                return payout.sum();
                                            }
                                        }
                                    )
                                ).doubleValue()
                            )
                    )
                )
            )
        ).asString();
    }

}
