package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.fake.FakePetition;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JsonPetitionTest {

    @Test
    public void test() throws BazisException {
        final Petition
            origin = new FakePetition(),
            converted = new JsonPetition(
                new JsonText(
                    new JsonText(new JsonPetition(origin)).asString()
                ).asJson()
            );
        MatcherAssert.assertThat(
            "test msp",
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
            "test reg date",
            converted.regDate(), Matchers.equalTo(origin.regDate())
        );
        MatcherAssert.assertThat(
            "test appoint date",
            converted.appointDate().has(),
            Matchers.equalTo(origin.appointDate().has())
        );
        MatcherAssert.assertThat(
            "test status",
            converted.status(), Matchers.equalTo(origin.status())
        );
    }

}