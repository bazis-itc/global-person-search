package bazis.utils.global_person_search.date;

import java.util.Date;

public final class HumanDate extends TextualDate {

    private static final String FORMAT = "dd.MM.yyyy";

    public HumanDate(Date date) {
        this(new FormattedDate(HumanDate.FORMAT, date));
    }

    public HumanDate(String date) {
        this(new FormattedDate(HumanDate.FORMAT, date));
    }

    private HumanDate(FormattedDate date) {
        super(date, date);
    }

}
