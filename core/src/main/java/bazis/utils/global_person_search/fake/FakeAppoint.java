package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.ConcatedText;
import java.util.Date;

@SuppressWarnings("ClassWithTooManyMethods")
public final class FakeAppoint implements Appoint {

    private final String type, msp, startDate, endDate;

    @SuppressWarnings("StringConcatenation")
    public FakeAppoint() {
        this.type = "37145780-704c-48a2-9272-1f99afddaa9f";
        this.msp =
            "Ежемесячная денежная компенсация военнослужащим, гражданам, " +
            "призванным на военные сборы, пенсионное обеспечение которых " +
            "осуществляется Пенсионным фондом Российской Федерации, и " +
            "членам их семей";
        this.startDate = "2019-01-01";
        this.endDate = "2019-12-31";
    }

    public FakeAppoint(String type, String msp) {
        this.type = type;
        this.msp = msp;
        this.startDate = "2019-01-01";
        this.endDate = "2019-12-31";
    }

    public FakeAppoint(String msp, String startDate, String endDate) {
        this.type = "37145780-704c-48a2-9272-1f99afddaa9f";
        this.msp = msp;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String type() {
        return this.type;
    }

    @Override
    public String msp() {
        return this.msp;
    }

    @Override
    @SuppressWarnings("StringConcatenation")
    public String category() {
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
        return FakeAppoint.dateFrom(this.startDate);
    }

    @Override
    public Opt<Date> endDate() throws BazisException {
        return FakeAppoint.dateFrom(this.endDate);
    }

    @Override
    public String payments() {
        return new UncheckedText(
            new ConcatedText(
                "13.07.2016 9877.52 Декабрь 2015, ",
                "21.04.2016 3572.67 Январь 2016, ",
                "21.04.2016 3572.67 Февраль 2016, "
            )
        ).asString();
    }

    private static Opt<Date> dateFrom(String date) throws BazisException {
        return date.isEmpty()
            ? new EmptyOpt<Date>()
            : new OptOf<>(new IsoDate(date).value());
    }

}
