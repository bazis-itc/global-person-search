package bazis.utils.global_person_search.jdbc;

import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.fake.FakeBorough;
import bazis.utils.global_person_search.printed.PrintedPeriods;
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
            final JdbcPerson person = new JdbcPerson(
                record,
                Collections.<Integer, Borough>singletonMap(
                    boroughId, new FakeBorough(borough)
                )
            );
            System.out.println("appoints:");
            for (final Appoint appoint : person.appoints())
                System.out.printf(
                    "%s, %s, %s%n",
                    appoint.msp(), appoint.category(),
                    new PrintedPeriods(appoint.periods()).asString()
                );
            System.out.println("petitions:");
            for (final Petition petition : person.petitions())
                System.out.printf(
                    "%s, %s%n",
                    petition.msp(), petition.category()
                );
        }
    }

}