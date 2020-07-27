package bazis.utils.global_person_search.json;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.dates.IsoDate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

@SuppressWarnings({"CyclicClassDependency", "ClassWithTooManyMethods"})
final class JsonPerson implements Person, Jsonable {

    private static final String
        FIO = "fio", BIRTHDATE = "birthdate", ADDRESS = "address",
        SNILS = "snils", BOROUGH = "borough", APPOINTS = "appoints",
        PASSPORT = "passport";

    private final Person origin;

    JsonPerson(JsonElement json) {
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
            JsonPerson.BIRTHDATE, new IsoDate(this.birthdate()).asString()
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

        private final JsonElement json;

        private Parsed(JsonElement json) {
            this.json = json;
        }

        @Override
        public String fio() {
            return this.string(JsonPerson.FIO);
        }

        @Override
        public Date birthdate() throws BazisException {
            return new IsoDate(this.string(JsonPerson.BIRTHDATE)).value();
        }

        @Override
        public String address() {
            return this.string(JsonPerson.ADDRESS);
        }

        @Override
        public String snils() {
            return this.string(JsonPerson.SNILS);
        }

        @Override
        public String borough() {
            return this.string(JsonPerson.BOROUGH);
        }

        @Override
        public String passport() {
            return this.string(JsonPerson.PASSPORT);
        }

        @Override
        public Iterable<Appoint> appoints() {
            return new MappedIterable<>(
                this.json
                    .getAsJsonObject()
                    .get(JsonPerson.APPOINTS)
                    .getAsJsonArray(),
                new Func<JsonElement, Appoint>() {
                    @Override
                    public Appoint apply(JsonElement item) {
                        return new JsonAppoint(item);
                    }
                }
            );
        }

        private String string(String property) {
            return this.json.getAsJsonObject().get(property).getAsString();
        }

    }

}
