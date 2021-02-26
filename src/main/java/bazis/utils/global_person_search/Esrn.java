package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import bazis.sitex3.SitexReport;
import java.io.File;
import java.util.Map;

public interface Esrn {

    Person person(Number id) throws BazisException;

    Map<String, String> measures(String links);

    SitexReport report(String code);

    String downloadUrl(File file);

    Number iteratorValue(String iterator) throws BazisException;

    String orgName() throws BazisException;

}
