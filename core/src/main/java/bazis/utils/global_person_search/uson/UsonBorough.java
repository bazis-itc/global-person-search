package bazis.utils.global_person_search.uson;

import bazis.cactoos3.Opt;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Borough;
import java.sql.ResultSet;
import java.util.Collection;
import org.jooq.Record;
import sx.bazis.uninfoobmen.sys.sql.ExecSelectRayon;

final class UsonBorough implements Borough {

    private final Record record;

    private final Collection<String> log;

    UsonBorough(Record record, Collection<String> log) {
        this.record = record;
        this.log = log;
    }

    @Override
    public Opt<ResultSet> select(final String query) {
        try {
            return new OptOf<ResultSet>(
                ExecSelectRayon.exec(
                    query,
                    this.record.getValue("url", String.class)
                )
            );
        } catch (final Exception ex) {
            this.log.add(
                String.format(
                    "Сервер не опрошен: %s",
                    this.record.getValue("name", String.class)
                )
            );
            return new EmptyOpt<>();
        }
    }
}
