package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.And;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.scalar.ObjectEquality;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ForkProtocol implements Protocol {

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final String fio;

    private final Date birthdate;

    private final Protocol complete;

    private final Protocol partially;

    public ForkProtocol(String fio, Date birthdate,
        Protocol complete, Protocol partially) {
        this.fio = fio;
        this.birthdate = birthdate;
        this.complete = complete;
        this.partially = partially;
    }

    @Override
    public void write(Person person) throws BazisException {
        (
            new CheckedScalar<>(
                new And(
                    new ObjectEquality<>(this.fio, person.fio()),
                    new ObjectEquality<>(
                        ForkProtocol.DATE_FORMAT.format(this.birthdate),
                        ForkProtocol.DATE_FORMAT.format(person.birthdate())
                    )
                )
            ).value() ? this.complete : this.partially
        ).write(person);
    }

}
