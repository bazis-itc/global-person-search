package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.CheckedScalar;

public final class CheckedFunc<T, R> implements Func<T, R> {
    
    private final Func<T, R> origin;

    public CheckedFunc(Func<T, R> origin) {
        this.origin = origin;
    }

    @Override
    public R apply(final T input) throws BazisException {
        return new CheckedScalar<>(
            new Scalar<R>() {
                @Override
                public R value() throws Exception {
                    return CheckedFunc.this.origin.apply(input);
                }
            }
        ).value();
    }

}
