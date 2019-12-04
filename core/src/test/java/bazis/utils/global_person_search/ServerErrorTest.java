package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.ext.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class ServerErrorTest {

    @Test
    public void test() throws BazisException {
        final String message = "something goes wrong";
        MatcherAssert.assertThat(
            new ServerError(
                new TextOf(
                    new ServerError(new BazisException(message)).asString()
                )
            ).value().getMessage(),
            Matchers.equalTo(message)
        );
    }

}