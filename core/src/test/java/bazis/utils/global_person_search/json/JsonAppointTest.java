package bazis.utils.global_person_search.json;

import bazis.cactoos3.text.FormattedText;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.ext.Lines;
import bazis.utils.global_person_search.fake.FakeAppoint;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonAppointTest {

    @Test
    public void payments() throws Exception {
        MatcherAssert.assertThat(
            "parsing",
            new JsonAppoint(
                new JsonText(
                    new FormattedText(
                        "{ \"payments\" = \"%s\" }",
                        new ConcatedText(
                            "13.07.2016 9877.50 Декабрь 2015, ",
                            "21.04.2016 3572.60 Январь 2016, ",
                            "21.04.2016 3572.60 Февраль 2016, "
                        ).asString()
                    )
                ).asJson().getAsJsonObject()
            ).payments(),
            Matchers.equalTo(
                new Lines(
                    "13.07.2016 9877.50 Декабрь 2015",
                    "21.04.2016 3572.60 Январь 2016",
                    "21.04.2016 3572.60 Февраль 2016"
                ).asString()
            )
        );
        MatcherAssert.assertThat(
            "printing",
            new JsonText(new JsonAppoint(new FakeAppoint())).asString(),
            Matchers.containsString(
                new ConcatedText(
                "\"payments\": \"",
                    "13.07.2016 9877.52 Декабрь 2015, ",
                    "21.04.2016 3572.67 Январь 2016, ",
                    "21.04.2016 3572.67 Февраль 2016, ",
                    "\""
                ).asString()
            )
        );
    }

}