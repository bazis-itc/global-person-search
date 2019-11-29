package bazis.utils.global_person_search.jsp;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.dates.HumanDate;
import java.util.Date;

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

    public String getStartDate() throws BazisException {
        final Opt<Date> date = this.appoint.startDate();
        return date.has() ? new HumanDate(date.get()).asString() : "";
    }

    public String getEndDate() throws BazisException {
        final Opt<Date> date = this.appoint.endDate();
        return date.has() ? new HumanDate(date.get()).asString() : "";
    }

    public String getPayments() {
        return this.appoint.payments();
    }

}
