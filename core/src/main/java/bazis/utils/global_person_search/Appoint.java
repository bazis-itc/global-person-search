package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import java.util.Date;

@SuppressWarnings("ClassWithTooManyMethods")
public interface Appoint {

    String type();

    String msp();

    String category();

    String child();

    String status();

    Opt<Date> startDate() throws BazisException;

    Opt<Date> endDate() throws BazisException;

    String payments();

}
