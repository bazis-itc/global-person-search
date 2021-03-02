package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.opt.OptOfNullable;
import java.util.Date;
import org.jooq.Record;

@Deprecated
final class SmartRecord {

    private final Record record;

    SmartRecord(Record record) {
        this.record = record;
    }

    public String string(String field) {
        final String value = this.record.getValue(field, String.class);
        return value == null ? "" : value;
    }

    public Opt<Date> date(String field) {
        return new OptOfNullable<>(this.record.getValue(field, Date.class));
    }

}
