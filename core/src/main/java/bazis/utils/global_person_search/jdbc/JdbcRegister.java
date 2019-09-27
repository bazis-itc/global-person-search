package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.Text;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Register;
import bazis.utils.global_person_search.ext.Lines;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.jooq.Record;
import org.jooq.impl.DSL;

public final class JdbcRegister implements Register {

    private final Connection connection;

    private final Map<Integer, Borough> boroughs;

    public JdbcRegister(Connection connection, Map<Integer, Borough> boroughs) {
        this.connection = connection;
        this.boroughs = boroughs;
    }

    @Override
    public Iterable<Person> persons(
        String fio, Opt<Date> birthdate, String snils) {
        final Text query = new Lines(
            "",
            "DECLARE",
            "  @fio VARCHAR(1000) = ?,",
            "  @birthdate DATETIME = CONVERT(DATETIME, ?, 120),",
            "  @snils VARCHAR(255) = ?",
            "",
            "SELECT ",
            "  surname.A_NAME AS surname,",
            "  name.A_NAME AS name,",
            "  patronymic.A_NAME AS patronymic,",
            "  person.BIRTHDATE AS birthdate,",
            "  person.A_REGFLAT AS address,",
            "  person.A_SERV AS boroughId,",
            "  person.A_LOCAL_OUID AS localId,",
            "  person.A_SNILS AS snils,",
            "  borough.A_RAION_NAME AS boroughName,",
            "  (",
            "    SELECT TOP 1 ",
            "      ISNULL(passport.DOCUMENTSERIES, '') + ' ' ",
            "      + ISNULL(passport.DOCUMENTSNUMBER, '')",
            "    FROM IDEN_DOC_REF_REGISTRY passport",
            "      JOIN SPR_DOC_STATUS status ",
            "        ON status.A_OUID = passport.A_DOCSTATUS",
            "          AND status.A_CODE = 'active'",
            "    WHERE passport.A_LD = person.A_OUID",
            "      AND ISNULL(passport.A_STATUS, 10) = 10",
            "    ORDER BY ",
            "      passport.ISSUEEXTENSIONSDATE DESC,",
            "      passport.A_CREATEDATE DESC,",
            "      passport.A_OUID DESC",
            "  ) AS passport",
            "FROM REGISTER_PERSONAL_CARD person",
            "  LEFT JOIN SPR_FIO_SURNAME surname ",
            "    ON surname.OUID = person.SURNAME",
            "  LEFT JOIN SPR_FIO_NAME name ",
            "    ON name.OUID = person.A_NAME",
            "  LEFT JOIN SPR_FIO_SECONDNAME patronymic ",
            "    ON patronymic.OUID = person.A_SECONDNAME",
            "  LEFT JOIN REFERENCE_INF borough ",
            "    ON borough.A_OUID = person.A_SERV",
            "WHERE ",
            "  surname.A_NAME + ' ' + name.A_NAME + ' ' ",
            "  + ISNULL(patronymic.A_NAME, '') = @fio",
            "  AND DATEDIFF(DAY, person.BIRTHDATE, @birthdate) = 0",
            "  OR REPLACE(REPLACE(person.A_SNILS, ' ', ''), '-', '') = @snils"
        );
        return new MappedIterable<>(
            DSL.using(this.connection).fetch(
                new UncheckedText(query).asString(),
                fio.isEmpty() ? null : fio,
                birthdate.has()
                    ? new SimpleDateFormat("yyyy-MM-dd")
                        .format(birthdate.get())
                    : null,
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
