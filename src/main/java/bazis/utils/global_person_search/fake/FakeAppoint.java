package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FakeAppoint implements Appoint {

    @Override
    public String msp() {
        return "Ежемесячная денежная компенсация военнослужащим, гражданам, " +
            "призванным на военные сборы, пенсионное обеспечение которых " +
            "осуществляется Пенсионным фондом Российской Федерации, и " +
            "членам их семей";
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
    public Opt<Date> startDate() {
        try {
            return new OptOf<>(
                new SimpleDateFormat("yyyy-MM-dd")
                    .parse("2019-01-01")
            );
        } catch (final ParseException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Opt<Date> endDate() {
        try {
            return new OptOf<>(
                new SimpleDateFormat("yyyy-MM-dd")
                    .parse("2019-12-31")
            );
        } catch (final ParseException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
