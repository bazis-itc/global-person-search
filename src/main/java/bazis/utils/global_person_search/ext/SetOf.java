package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.collection.CollectionEnvelope;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.scalar.CachedScalar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class SetOf<T extends Comparable<T>>
    extends CollectionEnvelope<T> implements Set<T> {

    public SetOf(T... items) {
        this(new IterableOf<>(items));
    }

    public SetOf(final Iterable<T> iterable) {
        super(
            new CachedScalar<>(
                new Scalar<Set<T>>() {
                    @Override
                    public Set<T> value() {
                        final Set<T> set = new HashSet<>(0);
                        for (final T item : iterable) set.add(item);
                        return Collections.unmodifiableSet(set);
                    }
                }
            )
        );
    }

}
