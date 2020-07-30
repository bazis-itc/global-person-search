package bazis.utils.global_person_search.misc;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class ConfigTest {

    @Test
    public void test() {
        MatcherAssert.assertThat(
            new Config().get("canCreateDoc"),
            Matchers.anyOf(
                Matchers.equalTo("true"),
                Matchers.equalTo("false")
            )
        );
    }

}