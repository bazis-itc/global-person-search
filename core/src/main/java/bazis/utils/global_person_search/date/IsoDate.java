package bazis.utils.global_person_search.date;

import java.util.Date;

public final class IsoDate extends DateEnvelope {

    private static final String FORMAT = "yyyy-MM-dd";

    public IsoDate(Date date) {
        this(new FormattedDate(IsoDate.FORMAT, date));
    }

    public IsoDate(String date) {
        this(new FormattedDate(IsoDate.FORMAT, date));
    }

    private IsoDate(FormattedDate date) {
        super(date, date);
    }

}
