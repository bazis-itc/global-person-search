package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import java.util.Date;
import org.jooq.Record;

@SuppressWarnings("ClassWithTooManyMethods")
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
    public Opt<Date> startDate() {
        return new SmartRecord(this.record).date("startDate");
    }

    @Override
    public Opt<Date> endDate() {
        return new SmartRecord(this.record).date("endDate");
    }

    @Override
    public String payments() {
        return new SmartRecord(this.record).string("payments");
    }

}
