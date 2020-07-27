package bazis.utils.global_person_search.jdbc;

import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.fake.FakeBorough;
import bazis.utils.global_person_search.misc.PrintedPayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collections;
import org.jooq.Record;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

@SuppressWarnings("IgnoredJUnitTest")
@Ignore
public final class JdbcPersonITCase {

    @Test
    @SuppressWarnings({
        "JUnitTestMethodWithNoAssertions",
        "UseOfSystemOutOrSystemErr",
        "CallToDriverManagerGetConnection",
        "MethodWithMultipleLoops"
    })
    public void test() throws Exception {
        final Integer boroughId = 123;
        final Record record = Mockito.mock(Record.class);
        Mockito
            .when(record.getValue(eq("boroughId"), any(Class.class)))
            .thenReturn(boroughId);
        Mockito
            .when(record.getValue(eq("localId"), any(Class.class)))
            .thenReturn("20758");
        try (
            final Connection borough = DriverManager.getConnection(
                new ConcatedText(
                    "jdbc:sqlserver://172.16.129.5;",
                    "databaseName=rab;",
                    "user=sa;password=S1tex2014"
                ).asString()
            )
        ) {
            for (
                final Appoint appoint :
                    new JdbcPerson(
                        record,
                        Collections.<Integer, Borough>singletonMap(
                            boroughId, new FakeBorough(borough)
                        )
                    ).appoints()
            ) {
                for (final Payout payout : appoint.payouts())
                    System.out.println(new PrintedPayout(payout).asString());
                System.out.println();
            }
        }
    }

}