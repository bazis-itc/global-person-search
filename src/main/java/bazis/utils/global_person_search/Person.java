package bazis.utils.global_person_search;

import bazis.cactoos3.Opt;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.utils.global_person_search.dates.IsoDate;
import bazis.utils.global_person_search.ext.MapEntry;
import java.util.Date;
import java.util.Map;

public interface Person {

    String fio() throws BazisException;

    Date birthdate() throws BazisException;

    String address() throws BazisException;

    String snils() throws BazisException;

    String borough() throws BazisException;

    String passport() throws BazisException;

    String status() throws BazisException;

    Map<String, String> regOff() throws BazisException;

    Iterable<Petition> petitions() throws BazisException;

    Iterable<Appoint> appoints() throws BazisException;

    final class RegOff extends MapEnvelope<String, String> {

        private static final String DATE = "date", REASON = "reason";

        public RegOff(final Person person) {
            this(
                new Scalar<Map<String, String>>() {
                    @Override
                    public Map<String, String> value() throws BazisException {
                        return person.regOff();
                    }
                }
            );
        }

        public RegOff(final Opt<Date> date, final String reason) {
            this(
                new Scalar<Map<String, String>>() {
                    @Override
                    public Map<String, String> value() throws BazisException {
                        return new MapOf<>(
                            new MapEntry<>(
                                Person.RegOff.DATE,
                                date.has()
                                    ? new IsoDate(date.get()).asString() : ""
                            ),
                            new MapEntry<>(Person.RegOff.REASON, reason)
                        );
                    }
                }
            );
        }

        private RegOff(Scalar<Map<String, String>> scalar) {
            super(new CachedScalar<>(scalar));
        }

        public Opt<Date> date() throws BazisException {
            final String date = this.get(Person.RegOff.DATE);
            return date.isEmpty()
                ? new EmptyOpt<Date>() : new OptOf<>(new IsoDate(date).value());
        }

        public String reason() {
            return this.get(Person.RegOff.REASON);
        }

    }

}
