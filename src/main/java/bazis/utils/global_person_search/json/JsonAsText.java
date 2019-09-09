package bazis.utils.global_person_search.json;

import bazis.cactoos3.Text;
import com.google.gson.GsonBuilder;

public final class JsonAsText implements Text {

    private final Jsonable json;

    public JsonAsText(Jsonable json) {
        this.json = json;
    }

    @Override
    public String asString() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(this.json.asJson());
    }

}
