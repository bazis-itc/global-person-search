package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableEnvelope;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Payout;

final class JdbcPayouts extends IterableEnvelope<Payout> {

    JdbcPayouts(final String payouts) {
        super(
            new Scalar<Iterable<Payout>>() {
                @Override
                public Iterable<Payout> value() {
                    //noinspection DynamicRegexReplaceableByCompiledPattern
                    return new MappedIterable<>(
                        payouts.isEmpty()
                            ? new EmptyIterable<String>()
                            : new IterableOf<>(payouts.split("[|]")),
                        new Func<String, Payout>() {
                            @Override
                            public Payout apply(String payout) {
                                return new JdbcPayout(payout);
                            }
                        }
                    );
                }
            }
        );
    }

}
