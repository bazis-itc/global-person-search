package bazis.utils.global_person_search.json;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings({"MethodReturnOfConcreteClass", "ClassWithTooManyMethods"})
public final class JsonRequest implements Jsonable {

    @SuppressWarnings("SpellCheckingInspection")
    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private static final String
        FIO = "fio", BIRTHDATE = "birthdate", SNILS = "snils";

    private final JsonObject json;

    public JsonRequest(JsonObject json) {
        this.json = json;
    }

    public JsonRequest withFio(String fio) throws BazisException {
        return this.with(JsonRequest.FIO, fio);
    }

    public String fio() {
        return this.property(JsonRequest.FIO);
    }

    public JsonRequest withBirthdate(Date date) throws BazisException {
        return this.with(
            JsonRequest.BIRTHDATE, JsonRequest.DATE_FORMAT.format(date)
        );
    }

    public Opt<Date> birthdate() throws BazisException {
        final String value = this.property(JsonRequest.BIRTHDATE);
        try {
            return value.isEmpty()
                ? new EmptyOpt<Date>()
                : new OptOf<>(JsonRequest.DATE_FORMAT.parse(value));
        } catch (final ParseException ex) {
            throw new BazisException(ex);
        }
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
        if (this.json.has(property)) throw new BazisException(
            String.format("Property '%s' already defined", property)
        );
        this.json.addProperty(property, value);
        return this;
    }

    private String property(String name) {
        return this.json.has(name) ? this.json.get(name).getAsString() : "";
    }

}
