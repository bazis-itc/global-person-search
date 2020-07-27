package bazis.utils.global_person_search.fake;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;

@SuppressWarnings("MagicNumber")
public final class FakePayout implements Payout {

    @Override
    public Date date() throws BazisException {
        return new IsoDate("2019-12-05").value();
    }

    @Override
    public Number year() {
        return 2016;
    }

    @Override
    public Number month() {
        return 7;
    }

    @Override
    public Number sum() {
        return 25.6;
    }

}
