package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.ext.ConcatedText;
import bazis.utils.global_person_search.fake.FakeBorough;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonText;
import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.Ignore;
import org.junit.Test;
import sx.common.MonthYearBean;

@SuppressWarnings("IgnoredJUnitTest")
@Ignore
public final class JdbcRegisterITCase {

    @Test
    @SuppressWarnings({
        "JUnitTestMethodWithNoAssertions",
        "CallToDriverManagerGetConnection",
        "UseOfSystemOutOrSystemErr"
    })
    public void test() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try (
            final Connection central = DriverManager.getConnection(
                new ConcatedText(
                "jdbc:sqlserver://10.65.12.5;",
                    "databaseName=update_test_central;",
                    "user=sa;password=S1tex2016"
                ).asString()
            );
            final Connection borough = DriverManager.getConnection(
                new ConcatedText(
                "jdbc:sqlserver://10.65.12.5;",
                    "databaseName=R15;",
                    "user=sa;password=S1tex2016"
                ).asString()
            )
        ) {
            final String json = new JsonText(
                new JsonPersons(
                    new JdbcRegister(
                        central,
                        new MapOf<>(
                            new Entry<Integer, Borough>(
                                15, new FakeBorough(borough)
                            ),
                            new Entry<Integer, Borough>(
                                24, new FakeBorough(borough)
                            )
                        )
                    ).persons(
                        "Абаева Валентина Николаевна",
                        new OptOf<>(
                            new MonthYearBean(
                                1947, 10, 20
                            ).getDate()
                        ),
                        ""
                    )
                )
            ).asString();
            final Iterable<Person> persons = new JsonPersons(
                new JsonText(json).asJson()
            );
            for (final Person person : persons) {
                System.out.println(person.fio());
                System.out.println(person.birthdate());
//                System.out.println(person.address());
//                System.out.println(person.snils());
//                System.out.println(person.borough());
//                System.out.println(person.passport());
//                System.out.println();
//                for (final Appoint appoint : person.appoints()) {
//                    System.out.println(appoint.category());
//                    System.out.println(appoint.child());
//                    System.out.println(appoint.msp());
//                    System.out.println(appoint.status());
//                    System.out.println(appoint.startDate().get());
//                    System.out.println(
//                        appoint.endDate().has()
//                            ? appoint.endDate().get() : "null"
//                    );
//                }
            }
        }
    }

}