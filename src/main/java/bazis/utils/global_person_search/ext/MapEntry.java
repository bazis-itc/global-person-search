package bazis.utils.global_person_search.ext;

import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.EntryEnvelope;

public final class MapEntry<K, V> extends EntryEnvelope<K, V> {

    public MapEntry(K key, V value) {
        super(new Entry<>(key, value));
    }

}
