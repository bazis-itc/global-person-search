package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.FilteredIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.opt.OptOrDefault;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Period;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.ext.Any;
import bazis.utils.global_person_search.ext.SetOf;
import java.util.Collection;
import java.util.Date;
import sx.admin.AdmRequest;
import sx.common.MonthYearBean;

public final class FilteredProtocol implements Protocol {

    private final Protocol origin;

    private final Iterable<String> msp;

    private final Date startDate, endDate;

    public FilteredProtocol(Protocol origin, Iterable<String> msp,
        Date startDate, Date endDate) {
        this.origin = origin;
        this.msp = msp;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Protocol append(Iterable<Person> persons) throws BazisException {
        return this.origin.append(
            new MappedIterable<>(
                persons,
                new Func<Person, Person>() {
                    @Override
                    public Person apply(Person person) {
                        return new FilteredPerson(
                            person,
                            FilteredProtocol.this.msp,
                            FilteredProtocol.this.startDate,
                            FilteredProtocol.this.endDate
                        );
                    }
                }
            )
        );
    }

    @Override
    public void outputTo(AdmRequest request) throws BazisException {
        this.origin.outputTo(request);
    }

}

final class FilteredPerson implements Person {

    private final Person origin;

    private final Iterable<String> msp;

    private final Date startDate, endDate;

    FilteredPerson(Person origin,
        Iterable<String> msp, Date startDate, Date endDate) {
        this.origin = origin;
        this.msp = msp;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String fio() throws BazisException {
        return this.origin.fio();
    }

    @Override
    public Date birthdate() throws BazisException {
        return this.origin.birthdate();
    }

    @Override
    public String address() throws BazisException {
        return this.origin.address();
    }

    @Override
    public String snils() throws BazisException {
        return this.origin.snils();
    }

    @Override
    public String borough() throws BazisException {
        return this.origin.borough();
    }

    @Override
    public String passport() throws BazisException {
        return this.origin.passport();
    }

    @Override
    public Iterable<Petition> petitions() throws BazisException {
        return new FilteredIterable<>(
            this.origin.petitions(),
            new Func<Petition, Boolean>() {
                @Override
                public Boolean apply(Petition petition) throws BazisException {
                    return
                        !new MonthYearBean(FilteredPerson.this.startDate)
                            .afterInDay(new MonthYearBean(petition.regDate()))
                        && !new MonthYearBean(FilteredPerson.this.endDate)
                            .beforeInDay(new MonthYearBean(petition.regDate()));
                }
            }
        );
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        //noinspection MismatchedQueryAndUpdateOfCollection
        final Collection<String> list = new SetOf<>(this.msp);
        return new FilteredIterable<>(
            new MappedIterable<>(
                this.origin.appoints(),
                new Func<Appoint, Appoint>() {
                    @Override
                    public Appoint apply(Appoint appoint) {
                        return new FilteredAppoint(
                            appoint,
                            FilteredPerson.this.startDate,
                            FilteredPerson.this.endDate
                        );
                    }
                }
            ),
            new Func<Appoint, Boolean>() {
                @Override
                public Boolean apply(Appoint appoint) throws Exception {
                    return (list.isEmpty() || list.contains(appoint.type()))
                        && FilteredPerson.this.checkPeriods(appoint);
                }
            }
        );
    }

    private boolean checkPeriods(Appoint appoint) throws Exception {
        return new Any<>(
            appoint.periods(),
            new Func<Period, Boolean>() {
                @Override
                public Boolean apply(Period period) throws BazisException {
                    return
                        !new MonthYearBean(FilteredPerson.this.startDate)
                            .afterInDay(
                                new MonthYearBean(
                                    new OptOrDefault<>(
                                        period.end(),
                                        FilteredPerson.this.startDate
                                    ).value()
                                )
                            )
                        && !new MonthYearBean(FilteredPerson.this.endDate)
                            .beforeInDay(
                                new MonthYearBean(
                                    new OptOrDefault<>(
                                        period.start(),
                                        FilteredPerson.this.endDate
                                    ).value()
                                )
                            );
                }
            }
        ).value();
    }

}

@SuppressWarnings("ClassWithTooManyMethods")
final class FilteredAppoint implements Appoint {

    private final Appoint origin;

    private final Date startDate, endDate;

    FilteredAppoint(Appoint origin, Date startDate, Date endDate) {
        this.origin = origin;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String type() {
        return this.origin.type();
    }

    @Override
    public String msp() {
        return this.origin.msp();
    }

    @Override
    public String category() {
        return this.origin.category();
    }

    @Override
    public String child() {
        return this.origin.child();
    }

    @Override
    public String status() {
        return this.origin.status();
    }

    @Override
    public Iterable<Period> periods() throws BazisException {
        return this.origin.periods();
    }

    @Override
    public Iterable<Payout> payouts() {
        return new FilteredIterable<>(
            this.origin.payouts(),
            new Func<Payout, Boolean>() {
                @Override
                public Boolean apply(Payout payout) {
                    final MonthYearBean date = new MonthYearBean(
                        payout.year().intValue(),
                        payout.month().intValue(),
                        1
                    );
                    return
                        !new MonthYearBean(FilteredAppoint.this.startDate)
                            .afterInDay(date)
                            && !new MonthYearBean(FilteredAppoint.this.endDate)
                            .beforeInDay(date);
                }
            }
        );
    }

}
