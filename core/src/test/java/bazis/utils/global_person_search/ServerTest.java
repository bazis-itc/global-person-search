package bazis.utils.global_person_search;

import bazis.utils.global_person_search.json.JsonAsText;
import bazis.utils.global_person_search.json.JsonRequest;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.LinkedList;
import sx.common.MonthYearBean;

public final class ServerTest {

    public static void main(String... args) throws Exception {
        System.out.println("2");
        final Collection<String> log = new LinkedList<>();
        final String response = new Server(
            "http://192.168.120.108:8080/central_test/", log
        ).send(
            new JsonAsText(
                new JsonRequest(new JsonObject())
                    .withFio("Абанина Ольга Александровна")
                    .withBirthdate(
                        new MonthYearBean(	1992, 2, 13)
                            .getDate()
                    )
                    .withSnils("")
            ).asString()
        );
        System.out.println(log);
        System.out.println(response);
    }

}
