package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.utils.global_person_search.jdbc.JdbcRegister;
import bazis.utils.global_person_search.json.JsonAsText;
import bazis.utils.global_person_search.json.JsonPersons;
import java.io.ByteArrayOutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import sx.bazis.uninfoobmen.sys.function.Function;
import sx.bazis.uninfoobmen.sys.store.DataObject;
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
        try {
            final String request = this.request(requestId);
            StoreFactory.getInstance().outMap.put(
                requestId, this.response(
                    new JsonAsText(
                        new JsonPersons(
                            new JdbcRegister(
                                SXDsFactory.getDs().getDb().getConnection()
                            ).persons(request)
                        )
                    ).asString()
                )
            );
            return super.getRetMessage(
                "COMPLETE",
                String.format("request is '%s'", request),
                null, requestId
            );
        } catch (final Exception ex) {
            return super.getRetMessage(
                "ERROR", "Ошибка", ex, requestId
            );
        }
    }

    private String request(String id) throws Exception {
        return IOUtils.toString(
            new DigestInputStream(
                StoreFactory.getInstance()
                    .inMap.get(id)
                    .getInputStream(),
                MessageDigest.getInstance("SHA-1")
            ),
            "CP1251"
        );
    }

    private DataObject response(String content) throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final MessageDigest crypto = MessageDigest.getInstance("SHA-1");
        IOUtils.write(
            content, new DigestOutputStream(output, crypto), "CP1251"
        );
        final DataObject response = new DataObject(
            Function.byteArrayToHexString(crypto.digest())
        );
        response.setRetByte(output.toByteArray());
        return response;
    }

}
