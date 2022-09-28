package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
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
import org.jooq.Result;

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
                            return new SmartRecord(JdbcPerson.this.record)
                                .string(field);
                        }
                    }
                )
            )
        ).asString();
    }

    @Override
    public Date birthdate() throws BazisException {
        final Opt<Date> date =
            new SmartRecord(this.record).date("birthdate");
        if (!date.has())
            throw new BazisException("Person birthdate not defined");
        return date.get();
    }

    @Override
    public String address() {
        return new SmartRecord(this.record).string("address");
    }

    @Override
    public String snils() {
        return new SmartRecord(this.record).string("snils");
    }

    @Override
    public String borough() {
        return new SmartRecord(this.record).string("boroughName");
    }

    @Override
    public String passport() {
        return new SmartRecord(this.record).string("passport");
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        final Text query = new Lines(
            "SELECT",
            "  [mspGuid] = msp.GUID,",
            "  [mspName] = msp.A_NAME,",
            "  [category] = category.A_NAME,",
            "  [status] = appointStatus.A_NAME,",
            "  [childSurname] = surname.A_NAME,",
            "  [childName] = name.A_NAME,",
            "  [childPatronymic] = patronymic.A_NAME,",
            "  [periods] = (",
            "    SELECT ",
            "      ISNULL(CONVERT(VARCHAR(10), term.STARTDATE, 120), 'NULL') + ' '",
            "      + ISNULL(CONVERT(VARCHAR(10), term.A_LASTDATE, 120), 'NULL') + '|'",
            "    FROM SPR_SERV_PERIOD term",
            "    WHERE term.A_SERV = appoint.OUID",
            "      AND ISNULL(term.A_STATUS, 10) = 10",
            "    FOR XML PATH ('')",
            "  ),",
            "  [payments] = (",
            "    SELECT ",
            "      CONVERT(VARCHAR(10), pay.PAIDDATE, 120) +  ' '",
            "      + CAST(pay.A_YEAR AS VARCHAR) + ' '",
            "      + CAST(payMonth.A_CODE AS VARCHAR) + ' '",
            "      + CAST(pay.AMOUNT AS VARCHAR) + '|'",
            "    FROM WM_PAY_CALC accrual",
            "      JOIN WM_PAIDAMOUNTS pay ON pay.A_PAYCALC = accrual.OUID",
            "        AND ISNULL(pay.A_STATUS, 10) = 10",
            "      JOIN SPR_STATUS_PAYMENT payStatus ",
            "        ON payStatus.A_ID = pay.A_STATUSPRIVELEGE",
            "      JOIN SPR_MONTH payMonth ON payMonth.A_ID = pay.A_MONTH",
            "    WHERE accrual.A_MSP = appoint.OUID",
            "      AND payStatus.A_CODE = 10 --Выплачено (закрыто)",
            "      AND ISNULL(accrual.A_STATUS, 10) = 10",
            "    FOR XML PATH ('')",
            "  )",
            "FROM ESRN_SERV_SERV appoint",
            "  LEFT JOIN SPR_NPD_MSP_CAT basement ",
            "    ON basement.A_ID = appoint.A_SERV",
            "  LEFT JOIN PPR_SERV msp ON basement.A_MSP = msp.A_ID",
            "  LEFT JOIN PPR_CAT category ",
            "    ON category.A_ID = basement.A_CATEGORY",
            "  LEFT JOIN WM_PERSONAL_CARD child ",
            "    LEFT JOIN SPR_FIO_SURNAME surname ",
            "      ON surname.OUID = child.SURNAME",
            "    LEFT JOIN SPR_FIO_NAME name ON name.OUID = child.A_NAME",
            "    LEFT JOIN SPR_FIO_SECONDNAME patronymic ",
            "      ON patronymic.OUID = child.A_SECONDNAME",
            "  ON child.OUID = appoint.A_CHILD",
            "  LEFT JOIN SPR_STATUS_PROCESS appointStatus ",
            "    ON appointStatus.A_ID = appoint.A_STATUSPRIVELEGE",
            "WHERE ISNULL(appoint.A_STATUS, 10) = 10",
            "  AND appoint.A_PERSONOUID = #id#"
        );
        final Borough borough = this.boroughs.get(
            this.record.getValue("boroughId", Integer.class)
        );
        if (borough == null) throw new BazisException("Borough not found");
        @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
        final Opt<Result<Record>> result = borough.select(
            new CheckedText(query).asString().replace(
                "#id#",
                this.record.getValue("localId", String.class)
            )
        );
        return result.has()
            ? result.get().map(
                new RecordMapper<Record, Appoint>() {
                    @Override
                    public Appoint map(Record appoint) {
                        return new JdbcAppoint(appoint);
                    }
                }
            )
            : new EmptyIterable<Appoint>();
    }

}
