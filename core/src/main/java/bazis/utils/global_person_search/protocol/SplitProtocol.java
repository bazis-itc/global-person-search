package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.And;
import bazis.cactoos3.scalar.ObjectEquality;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.ext.CheckedFunc;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import sx.admin.AdmRequest;

public final class SplitProtocol implements Protocol {

    @SuppressWarnings("SpellCheckingInspection")
    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final Protocol origin;

    private final CheckedFunc<Person, Boolean> predicate;

    public SplitProtocol(final Protocol origin, final Person person) {
        this(
            origin,
            new Func<Person, Boolean>() {
                @Override
                @SuppressWarnings({
                    "UnqualifiedStaticUsage", "AccessToNonThreadSafeStaticField"
                })
                public Boolean apply(Person compared) throws Exception {
                    return new And(
                        new ObjectEquality<>(compared.fio(), person.fio()),
                        new ObjectEquality<>(
                            DATE_FORMAT.format(compared.birthdate()),
                            DATE_FORMAT.format(person.birthdate())
                        )
                    ).value();
                }
            }
        );
    }

    private SplitProtocol(Protocol origin, Func<Person, Boolean> predicate) {
        this.origin = origin;
        this.predicate = new CheckedFunc<>(predicate);
    }

    @Override
    public Protocol append(Iterable<Person> persons) throws BazisException {
        final Collection<Person>
            primary = new LinkedList<>(), secondary = new LinkedList<>();
        for (final Person person : persons)
            (this.predicate.apply(person) ? primary : secondary).add(person);
        return this.origin
            .append(Collections.unmodifiableCollection(primary))
            .append(Collections.unmodifiableCollection(secondary));
    }

    @Override
    public void outputTo(AdmRequest request,
        Map<String, Object> params) throws BazisException {
        this.origin.outputTo(request, params);
    }

}
