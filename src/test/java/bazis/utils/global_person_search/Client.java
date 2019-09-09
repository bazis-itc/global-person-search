package bazis.utils.global_person_search;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;
import sx.bazis.uninfoobmen.sys.function.Function;
import sx.bazis.uninfoobmen.sys.function.ServerInfo;
import sx.bazis.uninfoobmen.sys.store.DataObject;
import sx.bazis.uninfoobmen.web.ClientConnectServlet;

public final class Client {

    public static void main(String... args) throws Exception {
        final String snils = "145-906-148 72";
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final MessageDigest crypto = MessageDigest.getInstance("SHA-1");
        IOUtils.write(
            snils, new DigestOutputStream(output, crypto),"CP1251"
        );
        final DataObject request = new DataObject(
            Function.byteArrayToHexString(crypto.digest())
        );
        request.setRetByte(output.toByteArray());
        final ClientConnectServlet connection = new ClientConnectServlet();
        try {
            final HashMap<String, String> params = new HashMap<>(3);
            params.put("cmd", "global_person_search");
            params.put("url", "http://10.65.12.11:8080/update_test_rayon/");
            params.put(
                "logInfo", ServerInfo.get("QUERY", "")
            );
            connection.setInputObject(request);
            System.out.println(
                connection.invoc(
                    "http://10.65.12.11:8080/update_test_central/", params
                )
            );
            System.out.println(
                IOUtils.toString(
                    new DigestInputStream(
                        connection.getOutputObject().getInputStream(),
                        MessageDigest.getInstance("SHA-1")
                    ),
                    "CP1251"
                )
            );
        } finally {
            connection.destroy();
        }
    }

}
