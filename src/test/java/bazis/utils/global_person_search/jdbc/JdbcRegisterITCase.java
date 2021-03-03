package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.ext.ConcatedText;
import java.util.Date;
import java.util.HashMap;
import org.jooq.impl.DSL;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("IgnoredJUnitTest")
@Ignore
public final class JdbcRegisterITCase {

    @Test
    public void test() throws Exception {
        final Iterable<Person> persons = new JdbcRegister(
            DSL.using(
                new ConcatedText(
                    "jdbc:sqlserver://172.17.60.6;",
                    "databaseName=central;",
                    "user=sa;password=Sitex2013!"
                ).asString()
            ),
            new EmptyMap<Integer, Borough>()
        ).persons("", new EmptyOpt<Date>(), "101-392-019 98");
        for (final Person person : persons) {
            System.out.println(person.fio());
            System.out.println(person.birthdate());
            System.out.println(person.address());
            System.out.println(person.snils());
            System.out.println(person.borough());
            System.out.println(person.passport());
            System.out.println(person.status());
            System.out.println(new HashMap<>(person.regOff()));
            System.out.println();
        }
    }

}