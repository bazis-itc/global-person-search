package bazis.utils.global_person_search.fake2;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.ext.Entries;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SuppressWarnings({"ClassWithTooManyMethods", "MethodReturnOfConcreteClass"})
public final class FakeAppoint implements Appoint {

    private static final String
        TYPE = "type", MSP = "msp",
        START_DATE = "startDate", END_DATE = "endDate";

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final Map<String, String> map;

    public FakeAppoint() {
        this(
            new MapOf<>(
                new Entry<>(FakeAppoint.TYPE,
                    "37145780-704c-48a2-9272-1f99afddaa9f"),
                new Entry<>(FakeAppoint.MSP,
                    "Ежемесячная денежная компенсация военнослужащим"),
                new Entry<>(FakeAppoint.START_DATE, "2019-01-01"),
                new Entry<>(FakeAppoint.END_DATE, "2019-12-31")
            )
        );
    }

    private FakeAppoint(Map<String, String> map) {
        this.map = map;
    }

    public FakeAppoint withType(String type) {
        return this.with(FakeAppoint.TYPE, type);
    }

    public FakeAppoint withMsp(String msp) {
        return this.with(FakeAppoint.MSP, msp);
    }

    public FakeAppoint withDates(String start, String end) {
        return this
            .with(FakeAppoint.START_DATE, start)
            .with(FakeAppoint.END_DATE, end);
    }

    private FakeAppoint with(String attr, String value) {
        return new FakeAppoint(
            new MapOf<>(
                new Entries<>(
                    this.map, new Entry<>(attr, value)
                )
            )
        );
    }

    @Override
    public String type() {
        return this.map.get(FakeAppoint.TYPE);
    }

    @Override
    public String msp() {
        return this.map.get(FakeAppoint.MSP);
    }

    @Override
    public String category() {
        //noinspection StringConcatenation
        return "Супруга (супруг), состоящая (состоящий) " +
            "на день гибели (смерти) военнослужащего, гражданина, " +
            "призванного на военные сборы, умерших вследствие военной травмы " +
            "в зарегистрированном браке с ним, не вступившая (не вступивший) " +
            "в повторный брак, достигшая возраста 50 лет " +
            "(достигший возраста 55 лет)";
    }

    @Override
    public String child() {
        return "Иванов Александр Дмитриевич";
    }

    @Override
    public String status() {
        return "Утверждено";
    }

    @Override
    public Opt<Date> startDate() throws BazisException {
        return this.date(FakeAppoint.START_DATE);
    }

    @Override
    public Opt<Date> endDate() throws BazisException {
        return this.date(FakeAppoint.END_DATE);
    }

    @Override
    public String payments() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    private Opt<Date> date(String key) throws BazisException {
        try {
            final String date = this.map.get(key);
            return date.isEmpty()
                ? new EmptyOpt<Date>()
                : new OptOf<>(FakeAppoint.DATE_FORMAT.parse(date));
        } catch (final ParseException ex) {
            throw new BazisException(ex);
        }
    }

}
