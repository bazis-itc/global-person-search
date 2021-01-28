package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.util.Date;

public interface Person {

    String fio() throws BazisException;

    Date birthdate() throws BazisException;

    String address() throws BazisException;

    String snils() throws BazisException;

    String borough() throws BazisException;

    String passport() throws BazisException;

    Iterable<Appoint> appoints() throws BazisException;

}
