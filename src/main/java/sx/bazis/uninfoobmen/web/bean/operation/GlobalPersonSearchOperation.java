package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.utils.global_person_search.EncryptedText;
import bazis.utils.global_person_search.jdbc.JdbcRegister;
import bazis.utils.global_person_search.json.JsonAsText;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.uson.UsonBoroughs;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpSession;
import sx.bazis.uninfoobmen.sys.store.StoreFactory;
import sx.datastore.SXDsFactory;

public final class GlobalPersonSearchOperation extends UIOperationBase {

    static {
        UIOperationFactory.registory(
            "global_person_search", GlobalPersonSearchOperation.class
        );
    }

    @Override
    public HashMap<String, String> exec(HashMap<String, String> inputMap,
        String requestId, HttpSession session) throws Exception {
        try (final Connection conn = SXDsFactory.getDs().getConnection()) {
            final List<String> log = new LinkedList<>();
            final String request = new EncryptedText(
                StoreFactory.getInstance()
                    .inMap.get(requestId)
                    .getInputStream()
            ).asString();
            StoreFactory.getInstance().outMap.put(
                requestId,
                new EncryptedText(
                    new JsonAsText(
                        new JsonPersons(
                            new JdbcRegister(
                                conn, new UsonBoroughs(conn, log)
                            ).persons(request)
                        )
                    )
                ).asBytes()
            );
            return super.getRetMessage(
                "COMPLETE",
                log.isEmpty() ? "" : log.get(0),
                null, requestId
            );
        } catch (final Exception ex) {
            return super.getRetMessage(
                "ERROR", ex.toString(), ex, requestId
            );
        }
    }

}
