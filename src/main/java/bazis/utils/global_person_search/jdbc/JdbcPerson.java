package bazis.utils.global_person_search.jdbc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.FilteredIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.ext.NoNulls;
import bazis.utils.global_person_search.ext.TextResource;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import org.jooq.Record;
import org.jooq.Result;

final class JdbcPerson implements Person {

    private final Record record;

    private final Iterable<Record> records;

    JdbcPerson(final Record record, final Map<Integer, Borough> boroughs) {
        this(
            record,
            new ListOf<>(
                new IterableOf<>(
                    new Scalar<Iterable<Record>>() {
                        @Override
                        public Iterable<Record> value() throws BazisException {
                            final Borough borough = boroughs.get(
                                record.getValue("boroughId", Integer.class)
                            );
                            if (borough == null)
                                throw new BazisException("Borough not found");
                            final Opt<Result<Record>> result = borough.select(
                                new CheckedText(
                                    new TextResource(
                                        JdbcPerson.class, "JdbcPerson.sql",
                                        Charset.forName("CP1251")
                                    )
                                ).asString().replace(
                                    "@id",
                                    record.getValue("localId", String.class)
                                )
                            );
                            return result.has()
                                ? result.get() : new EmptyIterable<Record>();
                        }
                    }
                )
            )
        );
    }

    private JdbcPerson(Record record, Iterable<Record> records) {
        this.record = record;
        this.records = records;
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
                            return new NoNulls(JdbcPerson.this.record)
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
            new NoNulls(this.record).date("birthdate");
        if (!date.has())
            throw new BazisException("Person birthdate not defined");
        return date.get();
    }

    @Override
    public String address() {
        return new NoNulls(this.record).string("address");
    }

    @Override
    public String snils() {
        return new NoNulls(this.record).string("snils");
    }

    @Override
    public String borough() {
        return new NoNulls(this.record).string("boroughName");
    }

    @Override
    public String passport() {
        return new NoNulls(this.record).string("passport");
    }

    @Override
    public String status() {
        return new NoNulls(this.record).string("status");
    }

    @Override
    public Map<String, String> regOff() {
        return new Person.RegOff(
            new NoNulls(this.record).date("regOffDate"),
            new NoNulls(this.record).string("regOffReason")
        );
    }

    @Override
    public Iterable<Petition> petitions() {
        return new MappedIterable<>(
            this.typed("petition"),
            new Func<Record, Petition>() {
                @Override
                public Petition apply(Record petition) {
                    return new JdbcPetition(petition);
                }
            }
        );
    }

    @Override
    public Iterable<Appoint> appoints() {
        return new MappedIterable<>(
            this.typed("appoint"),
            new Func<Record, Appoint>() {
                @Override
                public Appoint apply(Record appoint) {
                    return new JdbcAppoint(appoint);
                }
            }
        );
    }

    private Iterable<Record> typed(final String type) {
        return new FilteredIterable<>(
            this.records,
            new Func<Record, Boolean>() {
                @Override
                public Boolean apply(Record rec) {
                    return type.equals(
                        new NoNulls(rec).string("type")
                    );
                }
            }
        );
    }

}
