package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.OptOf;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("OverlyComplexBooleanExpression")
public final class DatePeriod {

    private static final DateFormat FORMAT =
        new SimpleDateFormat("dd.MM.yyyy");

    private final Opt<Date> from, to;

    public DatePeriod(Date start, Date end) {
        this(new OptOf<>(start), new OptOf<>(end));
    }

    public DatePeriod(Opt<Date> start, Opt<Date> end) {
        this.from = start;
        this.to = end;
    }

    public boolean contains(Date date) throws BazisException {
        DatePeriod.validate(this.from, this.to);
        return (
            !this.from.has()
            || DatePeriod.comparison(this.from.get(), date) <= 0
        ) && (
            !this.to.has()
            || DatePeriod.comparison(date, this.to.get()) <= 0
        );
    }

    public boolean overlap(Date start, Date end) throws BazisException {
        DatePeriod.validate(this.from, this.to);
        DatePeriod.validate(new OptOf<>(start), new OptOf<>(end));
        return (
            !this.from.has()
            || DatePeriod.comparison(this.from.get(), end) <= 0
        ) && (
            !this.to.has()
            || DatePeriod.comparison(start, this.to.get()) <= 0
        );
    }

    private static void validate(Opt<Date> start, Opt<Date> end)
        throws BazisException {
        if (
            start.has() && end.has()
            && DatePeriod.comparison(start.get(), end.get()) > 0
        ) throw new BazisException(
            String.format(
                "Incorrect period, start %s after than end %s",
                DatePeriod.FORMAT.format(start.get()),
                DatePeriod.FORMAT.format(end.get())
            )
        );
    }

    private static int comparison(Date left, Date right) {
        final int[]
            first = DatePeriod.fields(left),
            second = DatePeriod.fields(right);
        int result = 0;
        for (int index = 0; index < first.length; index++) {
            result = Integer.compare(first[index], second[index]);
            if (result != 0) break;
        }
        return result;
    }

    private static int[] fields(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new int[] {
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        };
    }

}
