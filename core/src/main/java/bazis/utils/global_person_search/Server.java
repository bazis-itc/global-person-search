package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import sx.bazis.uninfoobmen.web.ClientConnectServlet;
import sx.bazis.uninfoobmen.web.ConnectServletException;

final class Server {

    private final String url;

    Server(String url) {
        this.url = url;
    }

    String send(String request) throws BazisException {
        final ClientConnectServlet connection = new ClientConnectServlet();
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
            if ("ERROR".equals(result)) throw new BazisException(
                String.format("Server error: '%s'", response.get("ERROR_TITLE"))
            );
            if (!"COMPLETE".equals(result)) throw new BazisException(
                String.format("Unknown response type '%s'", result)
            );
            final String error = response.get("COMPLETE_TITLE");
            if (!error.isEmpty()) throw new BazisException(error);
            return new EncryptedText(
                connection.getOutputObject().getInputStream()
            ).asString();
        } catch (final FileNotFoundException ex) {
            throw new BazisException(ex);
        } catch (final ConnectServletException ex) {
            throw new BazisException(
                String.format("Сервер недоступен: %s", this.url), ex
            );
        } finally {
            try {
                connection.destroy();
            } catch (final IOException ex) {
                //noinspection ThrowFromFinallyBlock
                throw new BazisException(ex);
            }
        }
    }

}
