package bazis.utils.global_person_search.dates;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.scalar.UncheckedScalar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class SqlDate extends Number {

    private static final Number MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    private final UncheckedScalar<Number> scalar;

    public SqlDate(final Date date) {
        this.scalar = new UncheckedScalar<>(
            new Scalar<Number>() {
                @Override
                public Number value() throws ParseException {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    return TimeUnit.MILLISECONDS.toDays(
                        calendar.getTimeInMillis() -
                        new SimpleDateFormat("yyyy-MM-dd")
                            .parse("1900-01-01").getTime()
                    )
                        + (date.getTime() - calendar.getTimeInMillis())
                        / SqlDate.MILLIS_IN_DAY.doubleValue();
                }
            }
        );
    }

    @Override
    public int intValue() {
        return this.scalar.value().intValue();
    }

    @Override
    public long longValue() {
        return this.scalar.value().longValue();
    }

    @Override
    public float floatValue() {
        return this.scalar.value().floatValue();
    }

    @Override
    public double doubleValue() {
        return this.scalar.value().doubleValue();
    }

}
