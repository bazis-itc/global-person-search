package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Text;

public final class TextOf implements Text {
    
    private final String str;

    public TextOf(String str) {
        this.str = str;
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    public String asString() throws Exception {
        return this.str;
    }

}
