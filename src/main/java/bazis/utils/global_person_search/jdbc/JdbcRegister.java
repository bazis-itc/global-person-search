package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.CheckedText;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Register;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.TextResource;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Record;

public final class JdbcRegister implements Register {

    private final DSLContext context;

    private final Map<Integer, Borough> boroughs;

    public JdbcRegister(DSLContext context, Map<Integer, Borough> boroughs) {
        this.context = context;
        this.boroughs = boroughs;
    }

    @Override
    public Iterable<Person> persons(
        String fio, Opt<Date> birthdate, String snils) throws BazisException {
        return new MappedIterable<>(
            this.context.fetch(
                new CheckedText(
                    new TextResource(
                        JdbcRegister.class, "JdbcRegister.sql",
                        Charset.forName("CP1251")
                    )
                ).asString(),
                fio.isEmpty() ? null : fio,
                birthdate.has()
                    ? new IsoDate(birthdate.get()).asString() : null,
                snils.isEmpty() ? null : snils
            ),
            new Func<Record, Person>() {
                @Override
                public Person apply(Record record) {
                    return new JdbcPerson(
                        record, JdbcRegister.this.boroughs
                    );
                }
            }
        );
    }

}
