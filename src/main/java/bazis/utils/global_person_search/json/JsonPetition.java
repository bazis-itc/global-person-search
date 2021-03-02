package bazis.utils.global_person_search.json;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.dates.IsoDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

final class JsonPetition implements Petition, Jsonable {

    private static final String
        TYPE = "type", MSP = "msp", CATEGORY = "category",
        REG_DATE = "startDate", APPOINT_DATE = "endDate",
        STATUS = "status";

    private final Petition origin;

    JsonPetition(JsonElement json) {
        this(new JsonPetition.Parsed(json));
    }

    JsonPetition(Petition origin) {
        this.origin = origin;
    }

    @Override
    public String type() {
        return this.origin.type();
    }

    @Override
    public String msp() {
        return this.origin.msp();
    }

    @Override
    public String category() {
        return this.origin.category();
    }

    @Override
    public Date regDate() throws BazisException {
        return this.origin.regDate();
    }

    @Override
    public Opt<Date> appointDate() throws BazisException {
        return this.origin.appointDate();
    }

    @Override
    public String status() {
        return this.origin.status();
    }

    @Override
    public JsonElement asJson() throws BazisException {
        final JsonObject json = new JsonObject();
        json.addProperty(JsonPetition.TYPE, this.type());
        json.addProperty(JsonPetition.MSP, this.msp());
        json.addProperty(JsonPetition.CATEGORY, this.category());
        json.addProperty(
            JsonPetition.REG_DATE, new IsoDate(this.regDate()).asString()
        );
        json.addProperty(
            JsonPetition.APPOINT_DATE,
            this.appointDate().has()
                ? new IsoDate(this.appointDate().get()).asString() : ""
        );
        json.addProperty(JsonPetition.STATUS, this.status());
        return json;
    }

    private static final class Parsed implements Petition {

        private final JsonElement json;

        private Parsed(JsonElement json) {
            this.json = json;
        }

        @Override
        public String type() {
            return this.string(JsonPetition.TYPE);
        }

        @Override
        public String msp() {
            return this.string(JsonPetition.MSP);
        }

        @Override
        public String category() {
            return this.string(JsonPetition.CATEGORY);
        }

        @Override
        public Date regDate() throws BazisException {
            return new IsoDate(this.string(JsonPetition.REG_DATE)).value();
        }

        @Override
        public Opt<Date> appointDate() throws BazisException {
            final String date = this.string(JsonPetition.APPOINT_DATE);
            return date.isEmpty() ? new EmptyOpt<Date>()
                : new OptOf<>(new IsoDate(date).value());
        }

        @Override
        public String status() {
            return this.string(JsonPetition.STATUS);
        }

        private String string(String property) {
            return this.json.getAsJsonObject().get(property).getAsString();
        }

    }

}
