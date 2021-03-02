package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import java.util.Date;

public interface Petition {

    String type();

    String msp();

    String category();

    Date regDate() throws BazisException;

    Opt<Date> appointDate() throws BazisException;

    String status();

}
