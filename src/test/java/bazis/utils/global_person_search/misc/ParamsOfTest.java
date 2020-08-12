package bazis.utils.global_person_search.misc;

import bazis.utils.global_person_search.fake.FakeRequest;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class ParamsOfTest {

    @Test
    public void onePerson() {
        MatcherAssert.assertThat(
            new ParamsOf(
                new FakeRequest(
                    Collections.singletonMap("objId", "123@wmPersonalCard")
                )
            ).personId().get().intValue(),
            Matchers.equalTo(123)
        );
    }

    @Test
    public void twoPersons() {
        MatcherAssert.assertThat(
            new ParamsOf(
                new FakeRequest(
                    Collections.singletonMap(
                        "objId", "1@wmPersonalCard,2@wmPersonalCard"
                    )
                )
            ).personId().has(),
            Matchers.is(false)
        );
    }

    @Test
    public void folder() {
        MatcherAssert.assertThat(
            new ParamsOf(
                new FakeRequest(
                    Collections.singletonMap(
                        "objId", "10273135@SXFolder"
                    )
                )
            ).personId().has(),
            Matchers.is(false)
        );
    }

}