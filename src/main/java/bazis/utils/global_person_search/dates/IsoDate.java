package bazis.utils.global_person_search.dates;

import java.util.Date;

public final class IsoDate extends FormattedDate {

    private static final String FORMAT = "yyyy-MM-dd";

    public IsoDate(Date date) {
        super(IsoDate.FORMAT, date);
    }

    public IsoDate(String date) {
        super(IsoDate.FORMAT, date);
    }

}
