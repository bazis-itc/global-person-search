package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Period;
import java.util.Date;

public final class PeriodOf implements Period {

    private final Opt<Date> start, end;

    public PeriodOf(Opt<Date> start, Opt<Date> end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Opt<Date> start() throws BazisException {
        return this.start;
    }

    @Override
    public Opt<Date> end() throws BazisException {
        return this.end;
    }

}
