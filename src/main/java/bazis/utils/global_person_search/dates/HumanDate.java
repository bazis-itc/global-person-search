package bazis.utils.global_person_search.dates;

import java.util.Date;

public final class HumanDate extends FormattedDate {

    private static final String FORMAT = "dd.MM.yyyy";

    public HumanDate(Date date) {
        super(HumanDate.FORMAT, date);
    }

    public HumanDate(String date) {
        super(HumanDate.FORMAT, date);
    }

}
