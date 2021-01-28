package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Period;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

public final class JdbcAppointTest {

    @Test
    public void test() throws BazisException {
        MatcherAssert.assertThat(
            new JdbcAppoint(
                DSL.using(SQLDialect.DEFAULT)
                    .newRecord(DSL.field("periods", String.class))
                    .value1(
                        "NULL 2011-12-31|2012-01-01 2012-12-31|2013-01-01 NULL|"
                    )
            ).periods(),
            Matchers.<Period>iterableWithSize(3)
        );
    }

}