package bazis.utils.global_person_search.protocol.filtered;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.misc.ParamsOf;
import sx.admin.AdmRequest;

public final class FilteredProtocol implements Protocol {

    private final Protocol origin;

    private final Esrn esrn;

    private final Iterable<Iterable<Person>> lists;

    public FilteredProtocol(Protocol origin, Esrn esrn) {
        this(origin, esrn, new EmptyIterable<Iterable<Person>>());
    }

    private FilteredProtocol(Protocol origin, Esrn esrn,
        Iterable<Iterable<Person>> lists) {
        this.origin = origin;
        this.esrn = esrn;
        this.lists = lists;
    }

    @Override
    public Protocol append(Iterable<Person> persons) {
        return new FilteredProtocol(
            this.origin, this.esrn, new JoinedIterable<>(this.lists, persons)
        );
    }

    @Override
    public void outputTo(final AdmRequest request) throws BazisException {
        Protocol protocol = this.origin;
        final Iterable<String> measures =
            this.esrn.measures(new ParamsOf(request).msp()).keySet();
        for (final Iterable<Person> list : this.lists)
            protocol = protocol.append(
                new MappedIterable<>(
                    list,
                    new Func<Person, Person>() {
                        @Override
                        public Person apply(Person person)
                            throws BazisException {
                            return new FilteredPerson(
                                person, measures,
                                new ParamsOf(request).startDate(),
                                new ParamsOf(request).endDate()
                            );
                        }
                    }
                )
            );
        protocol.outputTo(request);
    }

}
