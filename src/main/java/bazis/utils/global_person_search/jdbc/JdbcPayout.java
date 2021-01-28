package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;

final class JdbcPayout implements Payout {

    private final String payout;

    JdbcPayout(String payout) {
        this.payout = payout;
    }

    @Override
    public Date date() throws BazisException {
        return new IsoDate(this.part(0)).value();
    }

    @Override
    public Number year() {
        return Integer.parseInt(this.part(1));
    }

    @Override
    public Number month() {
        return Integer.parseInt(this.part(2));
    }

    @Override
    public Number sum() {
        return Double.parseDouble(this.part(3));
    }

    private String part(int index) {
        return this.payout.split(" ")[index];
    }

}
