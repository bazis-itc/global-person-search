package bazis.utils.global_person_search.ext;

import bazis.cactoos3.iterable.IterableOf;

public final class Sum extends Number {

    private final Iterable<Number> numbers;

    public Sum(final Number... numbers) {
        this(new IterableOf<>(numbers));
    }

    public Sum(Iterable<Number> numbers) {
        this.numbers = numbers;
    }

    @Override
    public int intValue() {
        int sum = 0;
        for (final Number number : this.numbers)
            sum += number.intValue();
        return sum;
    }

    @Override
    public long longValue() {
        long sum = 0L;
        for (final Number number : this.numbers)
            sum += number.longValue();
        return sum;
    }

    @Override
    public float floatValue() {
        float sum = 0.0f;
        for (final Number number : this.numbers)
            sum += number.floatValue();
        return sum;
    }

    @Override
    public double doubleValue() {
        double sum = 0.0;
        for (final Number number : this.numbers)
            sum += number.doubleValue();
        return sum;
    }

}
