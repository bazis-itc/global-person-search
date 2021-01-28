package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Period;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.ext.Entries;
import bazis.utils.global_person_search.misc.PeriodOf;
import java.util.Date;
import java.util.Map;

@SuppressWarnings({"ClassWithTooManyMethods", "MethodReturnOfConcreteClass"})
public final class FakeAppoint implements Appoint {

    private static final String
        TYPE = "type", MSP = "msp",
        START_DATE = "startDate", END_DATE = "endDate";

    private final Map<String, String> map;

    private final Iterable<Payout> payouts;

    public FakeAppoint() {
        this(
            new MapOf<>(
                new Entry<>(FakeAppoint.TYPE,
                    "37145780-704c-48a2-9272-1f99afddaa9f"),
                new Entry<>(FakeAppoint.MSP,
                    "Ежемесячная денежная компенсация военнослужащим"),
                new Entry<>(FakeAppoint.START_DATE, "2019-01-01"),
                new Entry<>(FakeAppoint.END_DATE, "2019-12-31")
            ),
            new IterableOf<Payout>(
                new FakePayout(), new FakePayout(), new FakePayout()
            )
        );
    }

    private FakeAppoint(Map<String, String> map, Iterable<Payout> payouts) {
        this.map = map;
        this.payouts = payouts;
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

    public FakeAppoint withPayouts(Iterable<Payout> pays) {
        return new FakeAppoint(this.map, pays);
    }

    private FakeAppoint with(String attr, String value) {
        return new FakeAppoint(
            new MapOf<>(
                new Entries<>(
                    this.map, new Entry<>(attr, value)
                )
            ),
            this.payouts
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
        final Text text = new ConcatedText(
            "Супруга (супруг), состоящая (состоящий) ",
            "на день гибели (смерти) военнослужащего, гражданина, ",
            "призванного на военные сборы, умерших вследствие военной травмы ",
            "в зарегистрированном браке с ним, не вступившая (не вступивший) ",
            "в повторный брак, достигшая возраста 50 лет ",
            "(достигший возраста 55 лет)"
        );
        return new UncheckedText(text).asString();
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
    public Iterable<Period> periods() throws BazisException {
        return new IterableOf<Period>(
            new PeriodOf(
                this.date(FakeAppoint.START_DATE),
                this.date(FakeAppoint.END_DATE)
            )
        );
    }

    @Override
    public Iterable<Payout> payouts() {
        return this.payouts;
    }

    private Opt<Date> date(String key) throws BazisException {
        final String date = this.map.get(key);
        return date.isEmpty()
            ? new EmptyOpt<Date>()
            : new OptOf<>(new IsoDate(date).value());
    }

}
