package bazis.utils.global_person_search.json;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.dates.IsoDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

@SuppressWarnings({"MethodReturnOfConcreteClass", "ClassWithTooManyMethods"})
public final class JsonRequest implements Jsonable {

    private static final String
        FIO = "fio", BIRTHDATE = "birthdate", SNILS = "snils";

    private final JsonElement json;

    public JsonRequest(JsonElement json) {
        this.json = json;
    }

    public JsonRequest withFio(String fio) throws BazisException {
        return this.with(JsonRequest.FIO, fio);
    }

    public String fio() {
        return this.property(JsonRequest.FIO);
    }

    public JsonRequest withBirthdate(Date date) throws BazisException {
        return this.with(JsonRequest.BIRTHDATE, new IsoDate(date).asString());
    }

    public Opt<Date> birthdate() throws BazisException {
        final String value = this.property(JsonRequest.BIRTHDATE);
        return value.isEmpty()
            ? new EmptyOpt<Date>()
            : new OptOf<>(new IsoDate(value).value());
    }

    public JsonRequest withSnils(String snils) throws BazisException {
        return this.with(JsonRequest.SNILS, snils);
    }

    public String snils() {
        return this.property(JsonRequest.SNILS);
    }

    @Override
    public JsonElement asJson() {
        return this.json;
    }

    private JsonRequest with(String property, String value)
        throws BazisException {
        final JsonObject object = this.json.getAsJsonObject();
        if (object.has(property)) throw new BazisException(
            String.format("Property '%s' already defined", property)
        );
        object.addProperty(property, value);
        return this;
    }

    private String property(String name) {
        final JsonObject object = this.json.getAsJsonObject();
        return object.has(name) ? object.get(name).getAsString() : "";
    }

}
