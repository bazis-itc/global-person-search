package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.FilteredIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.cactoos3.text.FormattedText;
import bazis.cactoos3.text.JoinedText;
import bazis.sitex3.misc.ReportRow;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.ext.Sum;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final class RtfPerson extends MapEnvelope<String, Object> {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    RtfPerson(final Person person) {
        super(
            new CachedScalar<>(
                new Scalar<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> value() throws BazisException {
                        return RtfPerson.map(person);
                    }
                }
            )
        );
    }

    private static Map<String, Object> map(Person person)
        throws BazisException {
        final Person.RegOff regOff = new Person.RegOff(person);
        return new ReportRow()
            .withInt("personId", RtfPerson.COUNTER.getAndIncrement())
            .withString("borough", person.borough())
            .withString(
                "person",
                new JoinedText(
                    ", ",
                    new IterableOf<>(
                        person.fio(),
                        new HumanDate(person.birthdate()).asString(),
                        person.address()
                    )
                )
            )
            .withString(
                "passport", new FormattedText(
                    "%s, %s", person.snils(), person.passport()
                )
            )
            .withString(
                "personStatus",
                new JoinedText(
                    ", ",
                    new FilteredIterable<>(
                        new IterableOf<>(
                            person.status(),
                            regOff.date().has()
                                ? new HumanDate(regOff.date().get()).asString()
                                : "",
                            regOff.reason()
                        ),
                        new Func<String, Boolean>() {
                            @Override
                            public Boolean apply(String part) {
                                return !part.isEmpty();
                            }
                        }
                    )
                )
            )
            .withString(
                "sum",
                new FormattedText(
                    "%.02f",
                    new Sum(
                        new MappedIterable<>(
                            new JoinedIterable<>(
                                new MappedIterable<>(
                                    person.appoints(),
                                    new Func<Appoint, Iterable<Payout>>() {
                                        @Override
                                        public Iterable<Payout> apply(
                                            Appoint appoint) {
                                            return appoint.payouts();
                                        }
                                    }
                                )
                            ),
                            new Func<Payout, Number>() {
                                @Override
                                public Number apply(Payout payout) {
                                    return payout.sum();
                                }
                            }
                        )
                    ).doubleValue()
                )
            );
    }

}
