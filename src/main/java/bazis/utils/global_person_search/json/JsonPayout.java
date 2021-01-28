package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.IsoDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

final class JsonPayout implements Payout, Jsonable {

    private static final String
        DATE = "date", YEAR = "year", MONTH = "month", SUM = "sum";

    private final Payout origin;

    JsonPayout(JsonElement json) {
        this(new JsonPayout.Parsed(json));
    }

    JsonPayout(Payout origin) {
        this.origin = origin;
    }

    @Override
    public Date date() throws BazisException {
        return this.origin.date();
    }

    @Override
    public Number year() {
        return this.origin.year();
    }

    @Override
    public Number month() {
        return this.origin.month();
    }

    @Override
    public Number sum() {
        return this.origin.sum();
    }

    @Override
    public JsonElement asJson() throws BazisException {
        final JsonObject json = new JsonObject();
        json.addProperty(
            JsonPayout.DATE, new IsoDate(this.date()).asString()
        );
        json.addProperty(
            JsonPayout.YEAR, Integer.toString(this.year().intValue())
        );
        json.addProperty(
            JsonPayout.MONTH, Integer.toString(this.month().intValue())
        );
        json.addProperty(
            JsonPayout.SUM, Double.toString(this.sum().doubleValue())
        );
        return json;
    }

    private static final class Parsed implements Payout {

        private final JsonElement json;

        private Parsed(JsonElement json) {
            this.json = json;
        }

        @Override
        public Date date() throws BazisException {
            return new IsoDate(this.string(JsonPayout.DATE)).value();
        }

        @Override
        public Number year() {
            return Integer.parseInt(this.string(JsonPayout.YEAR));
        }

        @Override
        public Number month() {
            return Integer.parseInt(this.string(JsonPayout.MONTH));
        }

        @Override
        public Number sum() {
            return Double.parseDouble(this.string(JsonPayout.SUM));
        }

        private String string(String property) {
            return this.json.getAsJsonObject().get(property).getAsString();
        }

    }

}
