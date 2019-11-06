package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.io.File;
import java.util.Map;

public interface Report {

    @SuppressWarnings("UnusedReturnValue")
    Report append(Number group, Map<String, Object> row);

    File create(Map<String, Object> params) throws BazisException;

}
