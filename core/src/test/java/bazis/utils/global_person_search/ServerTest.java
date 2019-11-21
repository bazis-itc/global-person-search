package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import com.google.gson.JsonObject;
import sx.common.MonthYearBean;

public final class ServerTest {

    public static void main(String... args) throws BazisException {
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
