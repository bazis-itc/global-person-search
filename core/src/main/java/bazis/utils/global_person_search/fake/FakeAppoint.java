package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FakeAppoint implements Appoint {

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final String type, msp, startDate, endDate;

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

    private static Opt<Date> dateFrom(String date) throws BazisException {
        try {
            return date.isEmpty()
                ? new EmptyOpt<Date>()
                : new OptOf<>(FakeAppoint.DATE_FORMAT.parse(date));
        } catch (final ParseException ex) {
            throw new BazisException(ex);
        }
    }

}
