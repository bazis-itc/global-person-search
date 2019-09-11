package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.util.Date;

public interface Person {

    String fio();

    Date birthdate();

    String address();

    String snils();

    String borough();

    Iterable<Appoint> appoints() throws BazisException;

}
