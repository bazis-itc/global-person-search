package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;

public final class Any<T> implements Scalar<Boolean> {
    
    private final Iterable<T> iterable;
    
    private final Func<T, Boolean> predicate;

    public Any(Iterable<T> iterable, Func<T, Boolean> predicate) {
        this.iterable = iterable;
        this.predicate = predicate;
    }

    @Override
    public Boolean value() throws Exception {
        boolean result = false;
        for (final T item : this.iterable) 
            if (this.predicate.apply(item)) {
                result = true;
                break;
            }
        return result;
    }

}
