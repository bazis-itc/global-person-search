package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.iterable.IterableEnvelope;

public final class IterableOfScalar<T> extends IterableEnvelope<T> {

    public IterableOfScalar(Scalar<Iterable<T>> scalar) {
        super(scalar);
    }

}
