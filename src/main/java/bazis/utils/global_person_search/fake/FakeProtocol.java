package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.dates.HumanDate;
import java.util.Collection;
import sx.admin.AdmRequest;

public final class FakeProtocol implements Protocol {

    private final Collection<String> output;

    public FakeProtocol(Collection<String> output) {
        this.output = output;
    }

    @Override
    public Protocol append(Iterable<Person> persons) throws BazisException {
        final Text item = new JoinedText(
            ", ",
            new MappedIterable<>(
                persons,
                new Func<Person, String>() {
                    @Override
                    public String apply(Person person) throws BazisException {
                        return String.format(
                            "%s %s",
                            person.fio(),
                            new HumanDate(person.birthdate()).asString()
                        );
                    }
                }
            )
        );
        this.output.add(new CheckedText(item).asString());
        return this;
    }

    @Override
    public void outputTo(AdmRequest request)  throws BazisException {
        throw new BazisException("Method not implemented");
    }

}
