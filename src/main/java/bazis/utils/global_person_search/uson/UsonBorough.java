package bazis.utils.global_person_search.uson;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.utils.global_person_search.Borough;
import java.sql.ResultSet;
import sx.bazis.uninfoobmen.sys.sql.ExecSelectRayon;

final class UsonBorough implements Borough {

    private final String url;

    UsonBorough(String url) {
        this.url = url;
    }

    @Override
    public ResultSet select(final String query) throws BazisException {
        return new CheckedScalar<>(
            new Scalar<ResultSet>() {
                @Override
                public ResultSet value() throws Exception {
                    return ExecSelectRayon.exec(query, UsonBorough.this.url);
                }
            }
        ).value();
    }
}
