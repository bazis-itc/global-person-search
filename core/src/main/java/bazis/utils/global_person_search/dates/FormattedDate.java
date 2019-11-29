package bazis.utils.global_person_search.dates;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.scalar.ScalarOf;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattedDate implements Scalar<Date>, Text {

    private final String format;

    private final Scalar<Date> date;

    FormattedDate(final String format, final Date date) {
        this(format, new ScalarOf<>(date));
    }

    FormattedDate(final String format, final String date) {
        this(
            format,
            new Scalar<Date>() {
                @Override
                public Date value() throws ParseException {
                    return new SimpleDateFormat(format).parse(date);
                }
            }
        );
    }

    private FormattedDate(String format, Scalar<Date> date) {
        this.format = format;
        this.date = date;
    }

    @Override
    public final Date value() throws BazisException {
        return new CheckedScalar<>(this.date).value();
    }

    @Override
    public final String asString() throws BazisException {
        return new SimpleDateFormat(this.format).format(this.value());
    }

}
