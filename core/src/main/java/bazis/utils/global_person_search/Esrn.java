package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.util.Map;

public interface Esrn {

    Person person(Number id) throws BazisException;

    Map<String, String> measures(String links);

}
