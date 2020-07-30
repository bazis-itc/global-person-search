package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Borough;
import java.sql.ResultSet;
import java.util.Collection;
import org.jooq.Record;
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
    public Opt<ResultSet> select(final String query) throws BazisException {
        Opt<ResultSet> result;
        try {
            result = new OptOf<ResultSet>(
                ExecSelectRayon.exec(
                    query,
                    this.record.getValue("url", String.class)
                )
            );
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
