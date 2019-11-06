package bazis.utils.global_person_search;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.FilteredIterable;
import bazis.cactoos3.iterable.IterableEnvelope;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.opt.OptOrDefault;
import bazis.cactoos3.scalar.ScalarOf;
import bazis.utils.global_person_search.ext.SetOf;
import java.util.Collection;
import java.util.Date;
import sx.common.MonthYearBean;

final class RequestedPersons extends IterableEnvelope<Person> {

    RequestedPersons(final Iterable<Person> origin,
        final Iterable<String> msp, final Date startDate, final Date endDate) {
        super(
            new ScalarOf<>(
                new MappedIterable<>(
                    origin,
                    new Func<Person, Person>() {
                        @Override
                        public Person apply(Person person) {
                            return new RequestedPerson(
                                person, msp, startDate, endDate
                            );
                        }
                    }
                )
            )
        );
    }

}

final class RequestedPerson implements Person {

    private final Person origin;

    private final Iterable<String> msp;

    private final Date startDate, endDate;

    RequestedPerson(Person origin,
        Iterable<String> msp, Date startDate, Date endDate) {
        this.origin = origin;
        this.msp = msp;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String fio() {
        return this.origin.fio();
    }

    @Override
    public Date birthdate() throws BazisException {
        return this.origin.birthdate();
    }

    @Override
    public String address() {
        return this.origin.address();
    }

    @Override
    public String snils() {
        return this.origin.snils();
    }

    @Override
    public String borough() {
        return this.origin.borough();
    }

    @Override
    public String passport() {
        return this.origin.passport();
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        //noinspection MismatchedQueryAndUpdateOfCollection
        final Collection<String> list = new SetOf<>(this.msp);
        //noinspection OverlyComplexAnonymousInnerClass
        return new FilteredIterable<>(
            this.origin.appoints(),
            new Func<Appoint, Boolean>() {
                @Override
                public Boolean apply(Appoint appoint) throws BazisException {
                    //noinspection OverlyComplexBooleanExpression
                    return (list.isEmpty() || list.contains(appoint.type()))
                        && !new MonthYearBean(RequestedPerson.this.startDate)
                            .afterInDay(
                                new MonthYearBean(
                                    new OptOrDefault<>(
                                        appoint.endDate(),
                                        RequestedPerson.this.startDate
                                    ).value()
                                )
                            )
                        && !new MonthYearBean(RequestedPerson.this.endDate)
                            .beforeInDay(
                                new MonthYearBean(
                                    new OptOrDefault<>(
                                        appoint.startDate(),
                                        RequestedPerson.this.endDate
                                    ).value()
                                )
                            );
                }
            }
        );
    }

}
