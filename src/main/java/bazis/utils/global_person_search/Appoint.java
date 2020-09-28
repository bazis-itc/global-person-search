package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;

public interface Appoint {

    String type();

    String msp();

    String category();

    String child();

    String status();

    Iterable<Period> periods() throws BazisException;

    Iterable<Payout> payouts();

}
