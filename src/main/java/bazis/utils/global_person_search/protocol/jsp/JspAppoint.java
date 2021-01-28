package bazis.utils.global_person_search.protocol.jsp;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Period;
import bazis.utils.global_person_search.printed.PrintedPayout;
import bazis.utils.global_person_search.printed.PrintedPeriod;
import java.util.Collection;

public final class JspAppoint {

    private final Appoint appoint;

    JspAppoint(Appoint appoint) {
        this.appoint = appoint;
    }

    public String getMsp() {
        return this.appoint.msp();
    }

    public String getCategory() {
        return this.appoint.category();
    }

    public String getChild() {
        return this.appoint.child();
    }

    public String getStatus() {
        return this.appoint.status();
    }

    public Collection<String> getPeriods() throws BazisException {
        return new ListOf<>(
            new MappedIterable<>(
                this.appoint.periods(),
                new Func<Period, String>() {
                    @Override
                    public String apply(Period period) throws BazisException {
                        return new PrintedPeriod(period).asString();
                    }
                }
            )
        );
    }

    public Collection<String> getPayouts() {
        return new ListOf<>(
            new MappedIterable<>(
                this.appoint.payouts(),
                new Func<Payout, String>() {
                    @Override
                    public String apply(Payout payout) throws BazisException {
                        return new PrintedPayout(payout).asString();
                    }
                }
            )
        );
    }

}
