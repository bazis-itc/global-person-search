package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Func;
import bazis.cactoos3.iterable.IterableEnvelope;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.scalar.ScalarOf;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("ClassIndependentOfModule")
public final class Entries<K, V> extends IterableEnvelope<Map.Entry<K, V>> {

    public Entries(final Map<K, V> map, final Map.Entry<K, V>... entries) {
        this(map, new IterableOf<>(entries));
    }

    @SuppressWarnings("WeakerAccess")
    public Entries(final Map<K, V> map,
                   final Iterable<Map.Entry<K, V>> entries) {
        this(new JoinedIterable<>(new Entries<>(map), entries));
    }

    public Entries(final Map<K, V>... maps) {
        this(
            new JoinedIterable<>(
                new MappedIterable<>(
                    new IterableOf<>(maps),
                    new Func<Map<K, V>, Iterable<Map.Entry<K, V>>>() {
                        @Override
                        public Iterable<Map.Entry<K, V>> apply(Map<K, V> map) {
                            return map.entrySet();
                        }
                    }
                )
            )
        );
    }

    public <X, Y> Entries(final Map<X, Y> map,
                          final Func<X, K> keys, final Func<Y, V> values) {
        this(
            new Entries<>(map),
            new Func<Map.Entry<X, Y>, K>() {
                @Override
                public K apply(Map.Entry<X, Y> entry) throws Exception {
                    return keys.apply(entry.getKey());
                }
            },
            new Func<Map.Entry<X, Y>, V>() {
                @Override
                public V apply(Map.Entry<X, Y> entry) throws Exception {
                    return values.apply(entry.getValue());
                }
            }
        );
    }

    public <T> Entries(final Iterable<T> entries,
                       final Func<T, K> keys, final Func<T, V> values) {
        this(
            new MappedIterable<>(
                entries,
                new Func<T, Map.Entry<K, V>>() {
                    @Override
                    public Map.Entry<K, V> apply(T item) throws Exception {
                        return new Entry<>(
                            keys.apply(item),
                            values.apply(item)
                        );
                    }
                }
            )
        );
    }

    private Entries(final Map<K, V> map) {
        this(
            new Iterable<Map.Entry<K, V>>() {
                @Override
                public Iterator<Map.Entry<K, V>> iterator() {
                    return map.entrySet().iterator();
                }
            }
        );
    }

    private Entries(final Iterable<Map.Entry<K, V>> entries) {
        super(new ScalarOf<>(entries));
    }

}
