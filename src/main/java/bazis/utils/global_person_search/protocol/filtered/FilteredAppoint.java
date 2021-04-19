package bazis.utils.global_person_search.protocol.filtered;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.FilteredIterable;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Period;
import java.util.Date;
import sx.common.MonthYearBean;

final class FilteredAppoint implements Appoint {

    private final Appoint origin;

    private final Date startDate, endDate;

    FilteredAppoint(Appoint origin, Date startDate, Date endDate) {
        this.origin = origin;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String type() {
        return this.origin.type();
    }

    @Override
    public String msp() {
        return this.origin.msp();
    }

    @Override
    public String category() {
        return this.origin.category();
    }

    @Override
    public String child() {
        return this.origin.child();
    }

    @Override
    public String status() {
        return this.origin.status();
    }

    @Override
    public Iterable<Period> periods() throws BazisException {
        return this.origin.periods();
    }

    @Override
    public Iterable<Payout> payouts() {
        return new FilteredIterable<>(
            this.origin.payouts(),
            new Func<Payout, Boolean>() {
                @Override
                public Boolean apply(Payout payout) {
                    final MonthYearBean date = new MonthYearBean(
                        payout.year().intValue(),
                        payout.month().intValue(),
                        1
                    );
                    return
                        !new MonthYearBean(FilteredAppoint.this.startDate)
                            .afterInDay(date)
                        && !new MonthYearBean(FilteredAppoint.this.endDate)
                            .beforeInDay(date);
                }
            }
        );
    }

}
