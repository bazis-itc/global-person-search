package bazis.utils.global_person_search.jsp;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.dates.HumanDate;
import java.util.Collection;

public final class JspPerson {

    private final Person person;

    JspPerson(Person person) {
        this.person = person;
    }

    public String getFio() throws BazisException {
        return this.person.fio();
    }

    public String getBirthdate() throws BazisException {
        return new HumanDate(this.person.birthdate()).asString();
    }

    public String getAddress() throws BazisException {
        return this.person.address();
    }

    public String getSnils() throws BazisException {
        return this.person.snils();
    }

    public String getBorough() throws BazisException {
        return this.person.borough();
    }

    public String getPassport() throws BazisException {
        return this.person.passport();
    }

    @SuppressWarnings("MethodReturnOfConcreteClass")
    public Collection<JspAppoint> getAppoints() throws BazisException {
        return new ListOf<>(
            new MappedIterable<>(
                this.person.appoints(),
                new Func<Appoint, JspAppoint>() {
                    @Override
                    public JspAppoint apply(Appoint appoint) {
                        return new JspAppoint(appoint);
                    }
                }
            )
        );
    }

}
