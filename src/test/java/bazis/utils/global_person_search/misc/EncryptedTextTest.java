package bazis.utils.global_person_search.misc;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class EncryptedTextTest {

    @Test
    public void canCreateFromString() throws Exception {
        final String str = "some string";
        MatcherAssert.assertThat(
            new EncryptedText(str).asString(),
            Matchers.equalTo(str)
        );
    }

}