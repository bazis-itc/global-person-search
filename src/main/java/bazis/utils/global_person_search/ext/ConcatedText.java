package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Text;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.text.JoinedText;

public final class ConcatedText implements Text {

    private final Text text;

    public ConcatedText(String... strings) {
        this.text = new JoinedText("", new IterableOf<>(strings));
    }

    @Override
    public String asString() throws Exception {
        return this.text.asString();
    }

}
