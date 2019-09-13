package bazis.utils.global_person_search;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DefaultDateFormat extends DateFormat {

    private final DateFormat format;

    public DefaultDateFormat() {
        this.format = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public StringBuffer format(
        Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return this.format.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return this.format.parse(source, pos);
    }

}
