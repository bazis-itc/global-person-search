package bazis.utils.global_person_search.fake;

import bazis.cactoos3.iterable.IterableOf;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import java.util.Date;

public final class FakePerson implements Person {

    @Override
    public String fio() {
        return "Иванов Владислав Александрович";
    }

    @Override
    public Date birthdate() {
        return new Date();
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
    public Iterable<Appoint> appoints() {
        return new IterableOf<Appoint>(new FakeAppoint());
    }

}
