package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import java.util.Date;

public interface Appoint {

    String type();

    String msp();

    String category();

    String child();

    String status();

    Opt<Date> startDate();

    Opt<Date> endDate();

}
