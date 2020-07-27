package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.util.Date;

public interface Payout {

    Date date() throws BazisException;

    Number year();

    Number month();

    Number sum();

}
