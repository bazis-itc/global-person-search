package bazis.utils.global_person_search.printed;

import bazis.cactoos3.Opt;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Period;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.misc.PeriodOf;
import java.util.Date;

public final class PrintedPeriod implements Text {

    private final String separator;

    private final Period period;

    PrintedPeriod(Date start, Date end) {
        this(new OptOf<>(start), new OptOf<>(end));
    }

    PrintedPeriod(Opt<Date> start, Opt<Date> end) {
        this(new PeriodOf(start, end));
    }

    public PrintedPeriod(Period period) {
        this.separator = " ";
        this.period = period;
    }

    @Override
    public String asString() throws BazisException {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder(0)
            .append(this.withPrefix("с", this.period.start()))
            .append(
                this.period.start().has() && this.period.end().has()
                    ? this.separator : ""
            )
            .append(this.withPrefix("по", this.period.end()))
            .toString();
    }

    @SuppressWarnings("MethodMayBeStatic")
    private String withPrefix(String prefix, Opt<Date> date)
        throws BazisException {
        return date.has() ? String.format(
            "%s %s", prefix, new HumanDate(date.get()).asString()
        ) : "";
    }

}
