package bazis.utils.global_person_search.protocol.filtered;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.FilteredIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.opt.OptOrDefault;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Period;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.ext.Any;
import bazis.utils.global_person_search.ext.SetOf;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import sx.common.MonthYearBean;

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
    public String status() throws BazisException {
        return this.origin.status();
    }

    @Override
    public Map<String, String> regOff() throws BazisException {
        return this.origin.regOff();
    }

    @Override
    public Iterable<Petition> petitions() throws BazisException {
        //noinspection MismatchedQueryAndUpdateOfCollection
        final Collection<String> list = new SetOf<>(this.msp);
        return new FilteredIterable<>(
            this.origin.petitions(),
            new Func<Petition, Boolean>() {
                @Override
                public Boolean apply(Petition petition) throws BazisException {
                    //noinspection OverlyComplexBooleanExpression
                    return
                        (list.isEmpty() || list.contains(petition.type()))
                        && !new MonthYearBean(FilteredPerson.this.startDate)
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
