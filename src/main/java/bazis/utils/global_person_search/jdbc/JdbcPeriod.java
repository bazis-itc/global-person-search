package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Period;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;

final class JdbcPeriod implements Period {

    private final String period;

    JdbcPeriod(String period) {
        this.period = period;
    }

    @Override
    public Opt<Date> start() throws BazisException {
        return this.date(0);
    }

    @Override
    public Opt<Date> end() throws BazisException {
        return this.date(1);
    }

    private Opt<Date> date(int index) throws BazisException {
        final String date = this.period.split(" ")[index];
        return "NULL".equals(date)
            ? new EmptyOpt<Date>() : new OptOf<>(new IsoDate(date).value());
    }

}
