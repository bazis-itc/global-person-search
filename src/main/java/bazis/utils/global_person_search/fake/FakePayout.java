package bazis.utils.global_person_search.fake;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;

@SuppressWarnings("MagicNumber")
public final class FakePayout implements Payout {

    private final Number year, month;

    public FakePayout() {
        this(2016, 7);
    }

    public FakePayout(Number year, Number month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public Date date() throws BazisException {
        return new IsoDate("2019-12-05").value();
    }

    @Override
    public Number year() {
        return this.year;
    }

    @Override
    public Number month() {
        return this.month;
    }

    @Override
    public Number sum() {
        return 25.6;
    }

}
