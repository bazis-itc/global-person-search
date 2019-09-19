package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import java.sql.ResultSet;

public interface Borough {

    Opt<ResultSet> select(String query) throws BazisException;

}
