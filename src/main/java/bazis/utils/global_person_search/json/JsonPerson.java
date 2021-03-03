package bazis.utils.global_person_search.json;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.dates.IsoDate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.Map;

@SuppressWarnings({"CyclicClassDependency", "ClassWithTooManyMethods"})
final class JsonPerson implements Person, Jsonable {

    private static final String
        FIO = "fio", BIRTHDATE = "birthdate", ADDRESS = "address",
        SNILS = "snils", BOROUGH = "borough", PASSPORT = "passport",
        STATUS = "status", PETITIONS = "petitions", APPOINTS = "appoints",
        REG_OFF_DATE = "regOffDate", REG_OFF_REASON = "regOffReason";

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
    public String status() throws BazisException {
        return this.origin.status();
    }

    @Override
    public Map<String, String> regOff() throws BazisException {
        return this.origin.regOff();
    }

    @Override
    public Iterable<Petition> petitions() throws BazisException {
        return this.origin.petitions();
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
        json.addProperty(JsonPerson.STATUS, this.status());
        final Person.RegOff regOff = new Person.RegOff(this);
        json.addProperty(
            JsonPerson.REG_OFF_DATE,
            regOff.date().has()
                ? new IsoDate(regOff.date().get()).asString() : ""
        );
        json.addProperty(JsonPerson.REG_OFF_REASON, regOff.reason());

        final JsonArray appoints = new JsonArray();
        for (final Appoint appoint : this.appoints())
            appoints.add(new JsonAppoint(appoint).asJson());
        json.add(JsonPerson.APPOINTS, appoints);

        final JsonArray petitions = new JsonArray();
        for (final Petition petition : this.petitions())
            petitions.add(new JsonPetition(petition).asJson());
        json.add(JsonPerson.PETITIONS, petitions);

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
        public String status() {
            return this.string(JsonPerson.STATUS);
        }

        @Override
        public Map<String, String> regOff() throws BazisException {
            final String date = this.string(JsonPerson.REG_OFF_DATE);
            return new Person.RegOff(
                date.isEmpty()
                    ? new EmptyOpt<Date>()
                    : new OptOf<>(new IsoDate(date).value()),
                this.string(JsonPerson.REG_OFF_REASON)
            );
        }

        @Override
        public Iterable<Petition> petitions() {
            return new MappedIterable<>(
                this.json
                    .getAsJsonObject()
                    .get(JsonPerson.PETITIONS)
                    .getAsJsonArray(),
                new Func<JsonElement, Petition>() {
                    @Override
                    public Petition apply(JsonElement item) {
                        return new JsonPetition(item);
                    }
                }
            );
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
