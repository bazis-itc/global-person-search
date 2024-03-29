package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import java.util.Date;

public interface Register {

    Iterable<Person> persons(String fio, Opt<Date> birthdate, String snils)
        throws BazisException;

}
