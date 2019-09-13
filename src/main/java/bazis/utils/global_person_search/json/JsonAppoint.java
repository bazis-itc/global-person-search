package bazis.utils.global_person_search.json;

import bazis.cactoos3.Opt;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class JsonAppoint implements Appoint, Jsonable {

    private static final String
        TYPE = "type", MSP = "msp", CATEGORY = "category",
        CHILD = "child", STATUS = "status",
        START_DATE = "startDate", END_DATE = "endDate";

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final Appoint origin;

    JsonAppoint(JsonObject json) {
        this(new JsonAppoint.Parsed(json));
    }

    JsonAppoint(Appoint origin) {
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
    public String child() {
        return this.origin.child();
    }

    @Override
    public String status() {
        return this.origin.status();
    }

    @Override
    public Opt<Date> startDate() {
        return this.origin.startDate();
    }

    @Override
    public Opt<Date> endDate() {
        return this.origin.endDate();
    }

    @Override
    public JsonElement asJson() {
        final JsonObject json = new JsonObject();
        json.addProperty(JsonAppoint.TYPE, this.type());
        json.addProperty(JsonAppoint.MSP, this.msp());
        json.addProperty(JsonAppoint.CATEGORY, this.category());
        json.addProperty(JsonAppoint.CHILD, this.child());
        json.addProperty(JsonAppoint.STATUS, this.status());
        json.addProperty(
            JsonAppoint.START_DATE, this.dateAsText(this.startDate())
        );
        json.addProperty(
            JsonAppoint.END_DATE, this.dateAsText(this.endDate())
        );
        return json;
    }

    private String dateAsText(Opt<Date> date) {
        return date.has() ? JsonAppoint.DATE_FORMAT.format(date.get()) : null;
    }

    private static final class Parsed implements Appoint {

        private final JsonObject json;

        private Parsed(JsonObject json) {
            this.json = json;
        }

        @Override
        public String type() {
            return this.json.get(JsonAppoint.TYPE).getAsString();
        }

        @Override
        public String msp() {
            return this.json.get(JsonAppoint.MSP).getAsString();
        }

        @Override
        public String category() {
            return this.json.get(JsonAppoint.CATEGORY).getAsString();
        }

        @Override
        public String child() {
            return this.json.get(JsonAppoint.CHILD).getAsString();
        }

        @Override
        public String status() {
            return this.json.get(JsonAppoint.STATUS).getAsString();
        }

        @Override
        public Opt<Date> startDate() {
            return this.dateFrom(JsonAppoint.START_DATE);
        }

        @Override
        public Opt<Date> endDate() {
            return this.dateFrom(JsonAppoint.END_DATE);
        }

        private Opt<Date> dateFrom(String property) {
            try {
                final JsonElement element = this.json.get(property);
                return element == null
                    ? new EmptyOpt<Date>()
                    : new OptOf<>(
                        JsonAppoint.DATE_FORMAT.parse(element.getAsString())
                    );
            } catch (final ParseException ex) {
                throw new IllegalStateException(ex);
            }
        }

    }

}
