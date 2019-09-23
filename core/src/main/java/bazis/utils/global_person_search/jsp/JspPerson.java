package bazis.utils.global_person_search.jsp;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import java.text.SimpleDateFormat;

public final class JspPerson {

    private final Person person;

    public JspPerson(Person person) {
        this.person = person;
    }

    public String getFio() {
        return this.person.fio();
    }

    public String getBirthdate() {
        return new SimpleDateFormat("dd.MM.yyyy")
            .format(this.person.birthdate());
    }

    public String getAddress() {
        return this.person.address();
    }

    public String getSnils() {
        return this.person.snils();
    }

    public String getBorough() {
        return this.person.borough();
    }

    public String getPassport() {
        return this.person.passport();
    }

    public JspAppoint[] getAppoints() throws BazisException {
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
        ).toArray(new JspAppoint[0]);
    }

}
