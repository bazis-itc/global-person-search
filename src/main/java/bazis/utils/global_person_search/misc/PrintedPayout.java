package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.HumanDate;
import java.util.Map;

public final class PrintedPayout implements Text {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<Integer, String> MONTHS =
        new MapOf<>(
            new Entry<>(1, "Январь"),
            new Entry<>(2, "Февраль"),
            new Entry<>(3, "Март"),
            new Entry<>(4, "Апрель"),
            new Entry<>(5, "Май"),
            new Entry<>(6, "Июнь"),
            new Entry<>(7, "Июль"),
            new Entry<>(8, "Август"),
            new Entry<>(9, "Сентябрь"),
            new Entry<>(10, "Октябрь"),
            new Entry<>(11, "Ноябрь"),
            new Entry<>(12, "Декабрь")
        );

    private final Payout payout;

    public PrintedPayout(Payout payout) {
        this.payout = payout;
    }

    @Override
    public String asString() throws BazisException {
        final Iterable<String> parts = new IterableOf<>(
            new HumanDate(this.payout.date()).asString(),
            String.format("%.02f", this.payout.sum().doubleValue()),
            PrintedPayout.MONTHS.get(this.payout.month().intValue()),
            Integer.toString(this.payout.year().intValue())
        );
        return new CheckedText(
            new JoinedText(" ", parts)
        ).asString();
    }

}
