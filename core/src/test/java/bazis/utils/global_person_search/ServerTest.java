package bazis.utils.global_person_search;

import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.LinkedList;
import sx.common.MonthYearBean;

public final class ServerTest {

    public static void main(String... args) throws Exception {
        System.out.println("3");
        final Collection<String> log = new LinkedList<>();
        final String response = new Server(
            "http://192.168.120.108:8080/central_test/", log
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
        System.out.println(log);
        System.out.println(response);
    }

}
