package bazis.utils.global_person_search.misc;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import java.util.HashMap;
import java.util.Map;
import sx.bazis.uninfoobmen.web.ConnectServletException;
import sx.bazis.uninfoobmen.web.HttpMultipurposeClient;

@SuppressWarnings("deprecation")
public final class Server {

    private final String url;

    private String fails = "";

    public Server(String url) {
        this.url = url;
    }

    public String send(String request) throws BazisException {
        final HttpMultipurposeClient connection = new HttpMultipurposeClient();
        try {
            connection.setInputObject(new EncryptedText(request).asBytes());
            final Map<String, String> response = connection.invoc(
                this.url,
                new HashMap<>(
                    new MapOf<>(
                        new Entry<>("cmd", "global_person_search")
                        //unnecessary params:
                        //url : http://10.65.12.11:8080/update_test_rayon/
                        //logInfo : ServerInfo.get("QUERY", "")
                    )
                )
            );
            final String result = response.get("RESULT");
            if ("ERROR".equals(result))
                throw new BazisException(response.get("ERROR_TITLE"));
            if (!"COMPLETE".equals(result)) throw new BazisException(
                String.format("Unknown response type '%s'", result)
            );
            this.fails = response.get("COMPLETE_TITLE");
            return new EncryptedText(
                connection.getOutputObject().getInputStream()
            ).asString();
        } catch (final ConnectServletException ex) {
            throw new BazisException(
                String.format("Сервер недоступен: %s", this.url), ex
            );
        } finally {
            connection.destroy();
        }
    }

    public String fails() {
        return this.fails == null ? "" : this.fails;
    }

}
