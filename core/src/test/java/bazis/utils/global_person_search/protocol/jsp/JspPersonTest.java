package bazis.utils.global_person_search.protocol.jsp;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.fake.FakePerson;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JspPersonTest {

    @Test
    public void getBirthdate() throws BazisException {
        MatcherAssert.assertThat(
            new JspPerson(new FakePerson()).getBirthdate(),
            Matchers.equalTo("27.09.1990")
        );
    }

}