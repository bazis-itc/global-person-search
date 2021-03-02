package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;

public final class FakePetition implements Petition {

    private final Opt<Date> date;

    public FakePetition() {
        this(new EmptyOpt<Date>());
    }

    public FakePetition(Opt<Date> date) {
        this.date = date;
    }

    @Override
    public String msp() {
        return "Ежемесячная денежная компенсация за коммунальные услуги";
    }

    @Override
    public String category() {
        return "Ветеран боевых действий";
    }

    @Override
    public Date regDate() throws BazisException {
        return new IsoDate("2021-03-01").value();
    }

    @Override
    public Opt<Date> appointDate() {
        return this.date;
    }

    @Override
    public String status() {
        return "Утверждено";
    }

}
