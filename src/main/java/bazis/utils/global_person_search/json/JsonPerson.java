package bazis.utils.global_person_search.json;

import bazis.utils.global_person_search.Person;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class JsonPerson implements Person, Jsonable {

    private static final String
        FIO = "fio", BIRTHDATE = "birthdate", ADDRESS = "address";

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final Person origin;

    JsonPerson(JsonObject json) {
        this(new JsonPerson.Parsed(json));
    }

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
        json.addProperty(JsonPerson.FIO, this.fio());
        json.addProperty(
            JsonPerson.BIRTHDATE,
            JsonPerson.DATE_FORMAT.format(this.birthdate())
        );
        json.addProperty(JsonPerson.ADDRESS, this.address());
        return json;
    }

    private static final class Parsed implements Person {

        private final JsonObject json;

        private Parsed(JsonObject json) {
            this.json = json;
        }

        @Override
        public String fio() {
            return this.json.get(JsonPerson.FIO).getAsString();
        }

        @Override
        public Date birthdate() {
            try {
                return JsonPerson.DATE_FORMAT.parse(
                    this.json.get(JsonPerson.BIRTHDATE).getAsString()
                );
            } catch (final ParseException ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public String address() {
            return this.json.get(JsonPerson.ADDRESS).getAsString();
        }

    }

}
