package bazis.utils.global_person_search.date;

import java.util.Date;

public final class HumanDate extends DateEnvelope {

    private static final String FORMAT = "dd.MM.yyyy";

    public HumanDate(Date date) {
        super(HumanDate.FORMAT, date);
    }

    public HumanDate(String date) {
        super(HumanDate.FORMAT, date);
    }

}
