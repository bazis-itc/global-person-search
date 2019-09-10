package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.utils.global_person_search.EncryptedText;
import bazis.utils.global_person_search.jdbc.JdbcRegister;
import bazis.utils.global_person_search.json.JsonAsText;
import bazis.utils.global_person_search.json.JsonPersons;
import java.sql.Connection;
import java.util.HashMap;
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
                            new JdbcRegister(conn).persons(request)
                        )
                    )
                ).asBytes()
            );
            return super.getRetMessage(
                "COMPLETE",
                String.format("request is '%s'", request),
                null, requestId
            );
        } catch (final Exception ex) {
            return super.getRetMessage(
                "ERROR", ex.toString(), ex, requestId
            );
        }
    }

}
