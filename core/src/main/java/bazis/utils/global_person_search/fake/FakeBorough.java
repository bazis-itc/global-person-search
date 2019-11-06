package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Borough;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class FakeBorough implements Borough {

    private final Connection connection;

    public FakeBorough(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Opt<ResultSet> select(String query) throws BazisException {
        //noinspection LongLine
        try {
            //noinspection resource,JDBCResourceOpenedButNotSafelyClosed,JDBCExecuteWithNonConstantString
            return new OptOf<>(
                this.connection.createStatement().executeQuery(query)
            );
        } catch (final SQLException ex) {
            throw new BazisException(ex);
        }
    }

}
