package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.sql.ResultSet;

public interface Borough {

    ResultSet select(String query) throws BazisException;

}
