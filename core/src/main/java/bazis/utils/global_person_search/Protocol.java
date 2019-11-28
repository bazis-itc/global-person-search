package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.util.Map;
import sx.admin.AdmRequest;

public interface Protocol {

    Protocol append(Iterable<Person> persons) throws BazisException;

    void outputTo(AdmRequest request,
        Map<String, Object> params) throws BazisException;

}
