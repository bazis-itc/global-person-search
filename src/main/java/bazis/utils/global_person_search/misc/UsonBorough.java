package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Borough;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collection;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import sx.bazis.uninfoobmen.sys.sql.ExecSelectRayon;
import sx.common.SXUtils;
import sx.common.log.SXSysLog;

final class UsonBorough implements Borough {

    private final Record record;

    private final Collection<String> fails;

    UsonBorough(Record record, Collection<String> fails) {
        this.record = record;
        this.fails = fails;
    }

    @Override
    @SuppressWarnings("OverlyBroadCatchBlock")
    public Opt<Result<Record>> select(final String query) throws BazisException {
        Opt<Result<Record>> result;
        try (
            final ResultSet resultSet = ExecSelectRayon.executeQuery(
                query, this.record.getValue("url", String.class)
            )
        ) {
            final ResultSetMetaData meta = resultSet.getMetaData();
            final Field<?>[] fields = new Field<?>[meta.getColumnCount()];
            for (int index = 1; index <= meta.getColumnCount(); index++)
                fields[index - 1] = DSL.field(meta.getColumnName(index));
            final DSLContext context = DSL.using(SQLDialect.DEFAULT);
            final Result<Record> jooqResult = context.newResult(fields);
            while (resultSet.next()) {
                final Record record = context.newRecord(fields);
                for (int index = 1; index <= meta.getColumnCount(); index++)
                    record.setValue(
                        (Field<Object>) fields[index - 1],
                        resultSet.getObject(index)
                    );
                jooqResult.add(record);
            }
            result = new OptOf<Result<Record>>(jooqResult);
        } catch (final Throwable ex) {
            if (Boolean.parseBoolean(new Config().get("failSafe"))) {
                SXSysLog.error(
                    "globalPersonSearchLog", SXUtils.getStackTrace(ex),
                    null, false
                );
                this.fails.add(this.name());
                result = new EmptyOpt<>();
            } else throw new BazisException(
                String.format("Сервер не опрошен: %s", this.name()), ex
            );
        }
        return result;
    }

    private String name() {
        return this.record.getValue("name", String.class);
    }

}
