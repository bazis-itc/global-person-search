package bazis.utils.global_person_search.jsp;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Person;
import java.util.Collection;

public final class JspPersonList {

    private final String title;

    private final Iterable<Person> persons;

    JspPersonList(Iterable<Person> persons) {
        this("", persons);
    }

    JspPersonList(String title, Iterable<Person> persons) {
        this.title = title;
        this.persons = persons;
    }

    public String getTitle() {
        return this.title;
    }

    @SuppressWarnings("MethodReturnOfConcreteClass")
    public Collection<JspPerson> getPersons() {
        return new ListOf<>(
            new MappedIterable<>(
                this.persons,
                new Func<Person, JspPerson>() {
                    @Override
                    public JspPerson apply(Person person) {
                        return new JspPerson(person);
                    }
                }
            )
        );
    }

}
