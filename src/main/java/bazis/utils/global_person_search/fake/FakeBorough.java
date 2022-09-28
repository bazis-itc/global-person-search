package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Borough;
import java.sql.Connection;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public final class FakeBorough implements Borough {

    private final Connection connection;

    public FakeBorough(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Opt<Result<Record>> select(String query) throws BazisException {
        //noinspection LongLine
        try {
            //noinspection resource,JDBCResourceOpenedButNotSafelyClosed,JDBCExecuteWithNonConstantString
            return new OptOf<>(
                DSL.using(SQLDialect.DEFAULT).fetch(
                    this.connection.createStatement().executeQuery(query)
                )
            );
        } catch (final SQLException ex) {
            throw new BazisException(ex);
        }
    }

}
