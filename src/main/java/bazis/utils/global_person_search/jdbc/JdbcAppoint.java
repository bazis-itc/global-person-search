package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.opt.OptOfNullable;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import java.util.Date;
import org.jooq.Record;

final class JdbcAppoint implements Appoint {

    private final Record record;

    JdbcAppoint(Record record) {
        this.record = record;
    }

    @Override
    public String msp() {
        return this.record.getValue("mspName", String.class);
    }

    @Override
    public String category() {
        return this.record.getValue("category", String.class);
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
                            final String value = JdbcAppoint.this.record
                                .getValue(field, String.class);
                            return value == null ? "" : value;
                        }
                    }
                )
            )
        ).asString();
    }

    @Override
    public String status() {
        return this.record.getValue("status", String.class);
    }

    @Override
    public Opt<Date> startDate() {
        return new OptOfNullable<>(
            this.record.getValue("startDate", Date.class)
        );
    }

    @Override
    public Opt<Date> endDate() {
        return new OptOfNullable<>(
            this.record.getValue("endDate", Date.class)
        );
    }

}
