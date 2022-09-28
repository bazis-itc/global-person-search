package bazis.utils.global_person_search.misc;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import com.google.gson.JsonObject;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("IgnoredJUnitTest")
@Ignore
public final class ServerITCase {

    @Test
    @SuppressWarnings({
        "JUnitTestMethodWithNoAssertions", "UseOfSystemOutOrSystemErr"
    })
    public void test() throws BazisException {
        final String response = new Server(
            "http://10.0.0.1:8080/"
        ).send(
            new JsonText(
                new JsonRequest(new JsonObject()).withSnils("020-445-843-14")
            ).asString()
        );
        System.out.println(response);
    }

}
