package bazis.utils.global_person_search.date;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.text.CheckedText;
import java.util.Date;

abstract class TextualDate implements Scalar<Date>, Text {

    private final Scalar<Date> date;

    private final Text text;

    public TextualDate(Scalar<Date> date, Text text) {
        this.date = date;
        this.text = text;
    }

    @Override
    public final Date value() throws BazisException {
        return new CheckedScalar<>(this.date).value();
    }

    @Override
    public final String asString() throws BazisException {
        return new CheckedText(this.text).asString();
    }

}
