package bazis.utils.global_person_search.jsp;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.fake.FakeAppoint;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class JspAppointTest {

    @Test
    public void canGetDates() throws BazisException {
        final JspAppoint appoint = new JspAppoint(new FakeAppoint());
        MatcherAssert.assertThat(
            "can't get start date",
            appoint.getStartDate(), Matchers.equalTo("01.01.2019")
        );
        MatcherAssert.assertThat(
            "can't get end date",
            appoint.getEndDate(), Matchers.equalTo("31.12.2019")
        );
    }

    @Test
    public void canGetEmptyDates() throws BazisException {
        final JspAppoint appoint = new JspAppoint(
            new FakeAppoint().withDates("", "")
        );
        MatcherAssert.assertThat(
            "can't get empty start date",
            appoint.getStartDate(), Matchers.isEmptyString()
        );
        MatcherAssert.assertThat(
            "can't get empty end date",
            appoint.getEndDate(), Matchers.isEmptyString()
        );
    }

}