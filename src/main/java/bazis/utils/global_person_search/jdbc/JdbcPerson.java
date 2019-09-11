package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.ext.Lines;
import java.util.Date;
import java.util.Map;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

final class JdbcPerson implements Person {

    private final Record record;

    private final Map<Integer, Borough> boroughs;

    JdbcPerson(Record record, Map<Integer, Borough> boroughs) {
        this.record = record;
        this.boroughs = boroughs;
    }

    @Override
    public String fio() {
        return new UncheckedText(
            new JoinedText(
                " ",
                new MappedIterable<>(
                    new IterableOf<>("surname", "name", "patronymic"),
                    new Func<String, String>() {
                        @Override
                        public String apply(String field) {
                            final String value = JdbcPerson.this.record
                                .getValue(field, String.class);
                            return value == null ? "" : value;
                        }
                    }
                )
            )
        ).asString();
    }

    @Override
    public Date birthdate() {
        return this.record.getValue("birthdate", Date.class);
    }

    @Override
    public String address() {
        return this.record.getValue("address", String.class);
    }

    @Override
    public String snils() {
        return this.record.getValue("snils", String.class);
    }

    @Override
    public String borough() {
        return this.record.getValue("boroughName", String.class);
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        final Text query = new Lines(
            "",
            "SELECT",
            "  msp.GUID AS mspGuid,",
            "  msp.A_NAME AS mspName,",
            "  category.A_NAME AS category,",
            "  status.A_NAME AS status,",
            "  surname.A_NAME AS childSurname,",
            "  name.A_NAME AS childName,",
            "  patronymic.A_NAME AS childPatronymic,",
            "  period.startDate AS startDate,",
            "  period.endDate AS endDate",
            "FROM ESRN_SERV_SERV appoint",
            "  LEFT JOIN SPR_NPD_MSP_CAT basement ON basement.A_ID = appoint.A_SERV",
            "  LEFT JOIN PPR_SERV msp ON basement.A_MSP = msp.A_ID",
            "  LEFT JOIN PPR_CAT category ON category.A_ID = basement.A_CATEGORY",
            "  LEFT JOIN WM_PERSONAL_CARD child ",
            "    LEFT JOIN SPR_FIO_SURNAME surname ON surname.OUID = child.SURNAME",
            "    LEFT JOIN SPR_FIO_NAME name ON name.OUID = child.A_NAME",
            "    LEFT JOIN SPR_FIO_SECONDNAME patronymic ON patronymic.OUID = child.A_SECONDNAME",
            "  ON child.OUID = appoint.A_CHILD",
            "  LEFT JOIN SPR_STATUS_PROCESS status ON status.A_ID = appoint.A_STATUSPRIVELEGE",
            "  LEFT JOIN (",
            "    SELECT ",
            "      period.A_SERV AS appoint,",
            "      period.STARTDATE AS startDate,",
            "      period.A_LASTDATE AS endDate,",
            "      ROW_NUMBER() OVER (",
            "        PARTITION BY period.A_SERV ",
            "        ORDER BY period.STARTDATE DESC, period.A_CREATEDATE DESC",
            "      ) AS row",
            "    FROM SPR_SERV_PERIOD period",
            "    WHERE ISNULL(period.A_STATUS, 10) = 10",
            "  ) AS period ON period.appoint = appoint.OUID ",
            "    AND period.row = 1",
            "WHERE ISNULL(appoint.A_STATUS, 10) = 10",
            "  AND appoint.A_PERSONOUID = #id#"
        );
        final Borough borough = this.boroughs.get(
            this.record.getValue("boroughId", Integer.class)
        );
        if (borough == null) throw new BazisException("Borough not found");
        return DSL.using(SQLDialect.DEFAULT)
            .fetch(
                borough.select(
                    new CheckedText(query).asString().replace(
                        "#id#",
                        this.record.getValue("localId", String.class)
                    )
                )
            )
            .map(
                new RecordMapper<Record, Appoint>() {
                    @Override
                    public Appoint map(Record appoint) {
                        return new JdbcAppoint(appoint);
                    }
                }
            );
    }

}
