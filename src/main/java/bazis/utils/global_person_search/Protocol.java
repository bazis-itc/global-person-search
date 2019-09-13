package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;

public interface Protocol {

    void write(Person person) throws BazisException;

}
