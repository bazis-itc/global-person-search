package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import java.util.Date;

public interface Period {

    Opt<Date> start() throws BazisException;

    Opt<Date> end() throws BazisException;

}
