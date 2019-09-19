package bazis.utils.global_person_search.jsp;

import bazis.cactoos3.Opt;
import bazis.utils.global_person_search.Appoint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class JspAppoint {

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("dd.MM.yyyy");

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

    public String getStartDate() {
        final Opt<Date> date = this.appoint.startDate();
        return date.has() ? JspAppoint.DATE_FORMAT.format(date.get()) : "";
    }

    public String getEndDate() {
        final Opt<Date> date = this.appoint.endDate();
        return date.has() ? JspAppoint.DATE_FORMAT.format(date.get()) : "";
    }

}
