package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmRequest;

public interface Protocol {

    Protocol append(Iterable<Person> persons) throws BazisException;

    void outputTo(AdmRequest request) throws BazisException;

}
