package bazis.utils.global_person_search.json;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings({"CyclicClassDependency", "ClassWithTooManyMethods"})
final class JsonPerson implements Person, Jsonable {

    private static final String
        FIO = "fio", BIRTHDATE = "birthdate", ADDRESS = "address",
        SNILS = "snils", BOROUGH = "borough", APPOINTS = "appoints",
        PASSPORT = "passport";

    @SuppressWarnings("SpellCheckingInspection")
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
    public String fio() throws BazisException {
        return this.origin.fio();
    }

    @Override
    public Date birthdate() throws BazisException {
        return this.origin.birthdate();
    }

    @Override
    public String address() throws BazisException {
        return this.origin.address();
    }

    @Override
    public String snils() throws BazisException {
        return this.origin.snils();
    }

    @Override
    public String borough() throws BazisException {
        return this.origin.borough();
    }

    @Override
    public String passport() throws BazisException {
        return this.origin.passport();
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        return this.origin.appoints();
    }

    @Override
    public JsonElement asJson() throws BazisException {
        final JsonObject json = new JsonObject();
        json.addProperty(JsonPerson.FIO, this.fio());
        json.addProperty(
            JsonPerson.BIRTHDATE,
            JsonPerson.DATE_FORMAT.format(this.birthdate())
        );
        json.addProperty(JsonPerson.ADDRESS, this.address());
        json.addProperty(JsonPerson.SNILS, this.snils());
        json.addProperty(JsonPerson.BOROUGH, this.borough());
        json.addProperty(JsonPerson.PASSPORT, this.passport());
        final JsonArray appoints = new JsonArray();
        for (final Appoint appoint : this.appoints())
            appoints.add(new JsonAppoint(appoint).asJson());
        json.add(JsonPerson.APPOINTS, appoints);
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
        public Date birthdate() throws BazisException {
            try {
                return JsonPerson.DATE_FORMAT.parse(
                    this.json.get(JsonPerson.BIRTHDATE).getAsString()
                );
            } catch (final ParseException ex) {
                throw new BazisException(ex);
            }
        }

        @Override
        public String address() {
            return this.json.get(JsonPerson.ADDRESS).getAsString();
        }

        @Override
        public String snils() {
            return this.json.get(JsonPerson.SNILS).getAsString();
        }

        @Override
        public String borough() {
            return this.json.get(JsonPerson.BOROUGH).getAsString();
        }

        @Override
        public String passport() {
            return this.json.get(JsonPerson.PASSPORT).getAsString();
        }

        @Override
        public Iterable<Appoint> appoints() {
            return new MappedIterable<>(
                this.json.get(JsonPerson.APPOINTS).getAsJsonArray(),
                new Func<JsonElement, Appoint>() {
                    @Override
                    public Appoint apply(JsonElement item) {
                        return new JsonAppoint(item.getAsJsonObject());
                    }
                }
            );
        }

    }

}
