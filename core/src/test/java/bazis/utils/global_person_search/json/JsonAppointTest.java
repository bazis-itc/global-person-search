package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.text.FormattedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.ext.Lines;
import bazis.utils.global_person_search.fake.FakeAppoint;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonAppointTest {

    @Test
    public void canPrintAndParse() throws BazisException {
        final Appoint
            origin = new FakeAppoint(),
            converted = new JsonAppoint(
                new JsonText(
                    new JsonText(new JsonAppoint(origin)).asString()
                ).asJson()
            );
        MatcherAssert.assertThat(
            "test type",
            converted.type(), Matchers.equalTo(origin.type())
        );
        MatcherAssert.assertThat(
            "test msp",
            converted.msp(), Matchers.equalTo(origin.msp())
        );
        MatcherAssert.assertThat(
            "test category",
            converted.category(), Matchers.equalTo(origin.category())
        );
        MatcherAssert.assertThat(
            "test child",
            converted.child(), Matchers.equalTo(origin.child())
        );
        MatcherAssert.assertThat(
            "test status",
            converted.status(), Matchers.equalTo(origin.status())
        );
        MatcherAssert.assertThat(
            "test startDate",
            new IsoDate(converted.startDate().get()).asString(),
            Matchers.equalTo(
                new IsoDate(origin.startDate().get()).asString()
            )
        );
        MatcherAssert.assertThat(
            "test endDate",
            new IsoDate(converted.endDate().get()).asString(),
            Matchers.equalTo(
                new IsoDate(origin.endDate().get()).asString()
            )
        );
    }

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

    @Test
    public void emptyDates() throws BazisException {
        final Appoint
            origin = new FakeAppoint("some msp", "", ""),
            converted = new JsonAppoint(
                new JsonText(
                    new JsonText(new JsonAppoint(origin)).asString()
                ).asJson()
            );
        MatcherAssert.assertThat(
            "start date not empty",
            converted.startDate().has(), Matchers.is(false)
        );
        MatcherAssert.assertThat(
            "end date not empty",
            converted.endDate().has(), Matchers.is(false)
        );
    }

}