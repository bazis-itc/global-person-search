package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Person;
import java.util.Date;
import org.jooq.Record;

final class JdbcPerson implements Person {

    private final Record record;

    JdbcPerson(Record record) {
        this.record = record;
    }

    @Override
    public String fio() {
        return new UncheckedText(
            new JoinedText(
                " ",
                new MappedIterable<>(
                    new IterableOf<>("surname", "name", "patronymic"),
                    new Func<String, String>() {
                        @Override
                        public String apply(String field) {
                            final String value = JdbcPerson.this.record
                                .getValue(field, String.class);
                            return value == null ? "" : value;
                        }
                    }
                )
            )
        ).asString();
    }

    @Override
    public Date birthdate() {
        return this.record.getValue("birthdate", Date.class);
    }

    @Override
    public String address() {
        return this.record.getValue("address", String.class);
    }

}
