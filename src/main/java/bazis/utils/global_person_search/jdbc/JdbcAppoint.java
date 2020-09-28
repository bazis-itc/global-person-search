package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Period;
import org.jooq.Record;

final class JdbcAppoint implements Appoint {

    private final Record record;

    JdbcAppoint(Record record) {
        this.record = record;
    }

    @Override
    public String type() {
        return new SmartRecord(this.record).string("mspGuid");
    }

    @Override
    public String msp() {
        return new SmartRecord(this.record).string("mspName");
    }

    @Override
    public String category() {
        return new SmartRecord(this.record).string("category");
    }

    @Override
    public String child() {
        return new UncheckedText(
            new JoinedText(
                " ",
                new MappedIterable<>(
                    new IterableOf<>(
                        "childSurname", "childName", "childPatronymic"
                    ),
                    new Func<String, String>() {
                        @Override
                        public String apply(String field) {
                            return new SmartRecord(JdbcAppoint.this.record)
                                .string(field);
                        }
                    }
                )
            )
        ).asString();
    }

    @Override
    public String status() {
        return new SmartRecord(this.record).string("status");
    }

    @Override
    public Iterable<Period> periods() {
        final String periods = new SmartRecord(this.record).string("periods");
        return new MappedIterable<>(
            periods.isEmpty()
                ? new EmptyIterable<String>()
                : new IterableOf<>(periods.split("[|]")),
            new Func<String, Period>() {
                @Override
                public Period apply(String period) {
                    return new JdbcPeriod(period);
                }
            }
        );
    }

    @Override
    public Iterable<Payout> payouts() {
        return new JdbcPayouts(
            new SmartRecord(this.record).string("payments")
        );
    }

}
