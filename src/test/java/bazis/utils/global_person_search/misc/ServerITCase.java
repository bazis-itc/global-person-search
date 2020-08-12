package bazis.utils.global_person_search.misc;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import com.google.gson.JsonObject;
import org.junit.Ignore;
import org.junit.Test;
import sx.common.MonthYearBean;

@SuppressWarnings("IgnoredJUnitTest")
@Ignore
public final class ServerITCase {

    @Test
    @SuppressWarnings({
        "JUnitTestMethodWithNoAssertions", "UseOfSystemOutOrSystemErr"
    })
    public void test() throws BazisException {
        final String response = new Server(
            "http://192.168.120.108:8080/central_test/"
        ).send(
            new JsonText(
                new JsonRequest(new JsonObject())
                    .withFio("Абанин Николай Григорьевич")
                    .withBirthdate(
                        new MonthYearBean(	1950, 9, 19)
                            .getDate()
                    )
                    .withSnils("")
            ).asString()
        );
        System.out.println(response);
    }

}
