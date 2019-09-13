package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Text;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateAsText implements Text {

    private final String format;

    private final Date date;

    public DateAsText(String format, Date date) {
        this.format = format;
        this.date = date;
    }

    @Override
    public String asString() {
        return new SimpleDateFormat(this.format).format(this.date);
    }

}
