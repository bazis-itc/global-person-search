package bazis.utils.global_person_search.json;

import bazis.utils.global_person_search.Person;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Date;

final class JsonPerson implements Person, Jsonable {

    private final Person origin;

    JsonPerson(Person origin) {
        this.origin = origin;
    }

    @Override
    public String fio() {
        return this.origin.fio();
    }

    @Override
    public Date birthdate() {
        return this.origin.birthdate();
    }

    @Override
    public String address() {
        return this.origin.address();
    }

    @Override
    public JsonElement asJson() {
        final JsonObject json = new JsonObject();
        json.addProperty("fio", this.fio());
        json.addProperty(
            "birthdate",
            new SimpleDateFormat("yyyy-MM-dd").format(this.birthdate())
        );
        json.addProperty("address", this.address());
        return json;
    }

}
