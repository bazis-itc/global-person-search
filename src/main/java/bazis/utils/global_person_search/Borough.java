package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import org.jooq.Record;
import org.jooq.Result;

public interface Borough {

    Opt<Result<Record>> select(String query) throws BazisException;

}
