package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.utils.global_person_search.EncryptedText;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import sx.bazis.uninfoobmen.sys.store.StoreFactory;

public final class FakeOperation extends UIOperationBase {

    static {
        UIOperationFactory.registory(
            "fake_operation", FakeOperation.class
        );
    }

    @Override
    public HashMap<String, String> exec(HashMap<String, String> inputMap,
        String requestId, HttpSession session) throws Exception {
        try {
            String.format("%s");
            return super.getRetMessage(
                "COMPLETE",
                "operation complete",
                null, requestId
            );
        } catch (final Exception ex) {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            new ObjectOutputStream(output).writeObject(ex);
            StoreFactory.getInstance().outMap.put(
                requestId,
                new EncryptedText(
                    DatatypeConverter.printBase64Binary(output.toByteArray())
                ).asBytes()
            );
            return super.getRetMessage(
                "ERROR", "operation failed", ex, requestId
            );
        }
    }

}
