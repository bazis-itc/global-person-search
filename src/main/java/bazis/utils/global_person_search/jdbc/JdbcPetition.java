package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.ext.NoNulls;
import java.util.Date;
import org.jooq.Record;

final class JdbcPetition implements Petition {

    private final Record record;

    JdbcPetition(Record record) {
        this.record = record;
    }

    @Override
    public String msp() {
        return new NoNulls(this.record).string("mspName");
    }

    @Override
    public String category() {
        return new NoNulls(this.record).string("category");
    }

    @Override
    public Date regDate() throws BazisException {
        final Opt<Date> date = new NoNulls(this.record).date("regDate");
        if (!date.has())
            throw new BazisException("Petition don't has a reg date");
        return date.get();
    }

    @Override
    public Opt<Date> appointDate() {
        return new NoNulls(this.record).date("appointDate");
    }

    @Override
    public String status() {
        return new NoNulls(this.record).string("status");
    }

}
