package bazis.utils.global_person_search.dates;

import bazis.cactoos3.Opt;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.OptOf;
import java.util.Date;

public final class Period implements Text {

    private final String separator;

    private final Opt<Date> start, end;

    public Period(String separator, Date start, Date end) {
        this(separator, new OptOf<>(start), new OptOf<>(end));
    }

    public Period(String separator, Opt<Date> start, Opt<Date> end) {
        this.separator = separator;
        this.start = start;
        this.end = end;
    }

    @Override
    public String asString() throws BazisException {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder(0)
            .append(this.withPrefix("с", this.start))
            .append(this.start.has() && this.end.has() ? this.separator : "")
            .append(this.withPrefix("по", this.end))
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
