package bazis.utils.global_person_search.fake;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;

public final class FakePerson implements Person {

    private final String fio;

    private final Iterable<Appoint> appoints;

    public FakePerson() {
        this(
            "Иванов Владислав Александрович",
            new IterableOf<Appoint>(
                new FakeAppoint(), new FakeAppoint(), new FakeAppoint()
            )
        );
    }

    public FakePerson(String fio, Appoint... appoints) {
        this(fio, new IterableOf<>(appoints));
    }

    @SuppressWarnings("WeakerAccess")
    public FakePerson(String fio, Iterable<Appoint> appoints) {
        this.fio = fio;
        this.appoints = appoints;
    }

    @Override
    public String fio() {
        return this.fio;
    }

    @Override
    public Date birthdate() throws BazisException {
        return new IsoDate("1990-09-27").value();
    }

    @Override
    public String address() {
        return "г. Саранск, ул. Лодыгина, д. 3";
    }

    @Override
    public String snils() {
        return "048-592-353 92";
    }

    @Override
    public String borough() {
        return "Пролетарский район";
    }

    @Override
    public String passport() {
        return "6804 162300";
    }

    @Override
    public Iterable<Appoint> appoints() {
        return this.appoints;
    }

}
