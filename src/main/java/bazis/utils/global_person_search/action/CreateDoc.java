package bazis.utils.global_person_search.action;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.misc.ParamsOf;
import bazis.utils.global_person_search.protocol.DocProtocol;
import bazis.utils.global_person_search.protocol.ProtocolWithFilter;
import bazis.utils.global_person_search.protocol.SplitProtocol;
import java.sql.Connection;
import java.sql.SQLException;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import sx.admin.AdmRequest;
import sx.datastore.SXDsFactory;
import sx.datastore.db.SXDb;

public final class CreateDoc implements SitexAction {

    private final Esrn esrn;

    public CreateDoc(Esrn esrn) {
        this.esrn = esrn;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        Connection connection = null;
        try {
            connection = SXDsFactory.getDs().getDb().getConnection();
            new ProtocolWithFilter(
                new SplitProtocol(
                    new DocProtocol(DSL.using(connection, SQLDialect.DEFAULT), this.esrn),
                    this.esrn.person(new ParamsOf(request).objId())
                ),
                this.esrn
            )
                .append(
                    new JsonPersons(
                        new JsonText(request.getParam("persons")).asJson()
                    )
                )
                .outputTo(request);
            request.set("message", "Документ сформирован");
        } catch (final Exception ex) {
            throw new BazisException("Не удалось создать документ", ex);
        } finally {
            try {
                SXDb.closeConnection(connection);
            } catch (final SQLException ex) {
                //ignore
            }
        }
    }

}
