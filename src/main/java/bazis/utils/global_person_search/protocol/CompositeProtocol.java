package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;

public final class CompositeProtocol implements Protocol {

    private final Iterable<Protocol> protocols;

    public CompositeProtocol(Protocol... protocols) {
        this.protocols = new IterableOf<>(protocols);
    }

    @Override
    public void write(Person person) throws BazisException {
        for (final Protocol protocol : this.protocols) protocol.write(person);
    }

}
