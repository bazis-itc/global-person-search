package bazis.utils.global_person_search.jdbc;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jooq.Record;
import org.junit.Test;
import org.mockito.Mockito;

public final class JdbcAppointTest {

    @Test
    public void test() {
        final String
            msp = "msp name",
            category = "category name";
        final Record record = Mockito.mock(Record.class);
        Mockito.when(
            record.getValue(
                Mockito.eq("mspName"), Mockito.<Class<?>>any()
            )
        ).thenReturn(msp);
        Mockito.when(
            record.getValue(
                Mockito.eq("category"), Mockito.<Class<?>>any()
            )
        ).thenReturn(category);
        MatcherAssert.assertThat(
            "test appoint msp",
            new JdbcAppoint(record).msp(),
            Matchers.equalTo(msp)
        );
        MatcherAssert.assertThat(
            "test appoint category",
            new JdbcAppoint(record).category(),
            Matchers.equalTo(category)
        );
    }

}