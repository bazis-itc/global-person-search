package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Register;
import bazis.utils.global_person_search.ext.Lines;
import java.sql.Connection;
import org.jooq.Record;
import org.jooq.impl.DSL;

public final class JdbcRegister implements Register {

    private final Connection connection;

    public JdbcRegister(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Iterable<Person> persons(String snils) {
        final Text query = new Lines(
            "SELECT ",
            "  surname.A_NAME AS surname,",
            "  name.A_NAME AS name,",
            "  secondName.A_NAME AS patronymic,",
            "  person.BIRTHDATE AS birthdate,",
            "  person.A_REGFLAT AS address",
            "FROM REGISTER_PERSONAL_CARD person",
            "  LEFT JOIN SPR_FIO_SURNAME surname ",
            "    ON surname.OUID = person.SURNAME",
            "  LEFT JOIN SPR_FIO_NAME name ON name.OUID = person.A_NAME",
            "  LEFT JOIN SPR_FIO_SECONDNAME secondName ",
            "    ON secondName.OUID = person.A_SECONDNAME",
            "WHERE person.A_SNILS = '#snils#'"
        );
        return new MappedIterable<>(
            DSL.using(this.connection).fetch(
                new UncheckedText(query).asString()
                    .replace("#snils#", snils)
            ),
            new Func<Record, Person>() {
                @Override
                public Person apply(Record record) {
                    return new JdbcPerson(record);
                }
            }
        );
    }

}
