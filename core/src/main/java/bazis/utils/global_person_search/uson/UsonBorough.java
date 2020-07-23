package bazis.utils.global_person_search.uson;

import bazis.cactoos3.Opt;
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
    public Opt<ResultSet> select(final String query) {
        Opt<ResultSet> result;
        try {
            result = new OptOf<ResultSet>(
                ExecSelectRayon.exec(
                    query,
                    this.record.getValue("url", String.class)
                )
            );
        } catch (final Throwable ex) {
            SXSysLog.error(
                "globalPersonSearchLog", SXUtils.getStackTrace(ex),
                null, false
            );
            this.fails.add(this.record.getValue("name", String.class));
            result = new EmptyOpt<>();
        }
        return result;
    }
}
