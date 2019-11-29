package bazis.utils.global_person_search.json;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings({"CyclicClassDependency", "ClassWithTooManyMethods"})
final class JsonAppoint implements Appoint, Jsonable {

    private static final String
        TYPE = "type", MSP = "msp", CATEGORY = "category",
        CHILD = "child", STATUS = "status",
        START_DATE = "startDate", END_DATE = "endDate",
        PAYMENTS = "payments";

    @SuppressWarnings("SpellCheckingInspection")
    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");

    private final Appoint origin;

    JsonAppoint(JsonElement json) {
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
    public Opt<Date> startDate() throws BazisException {
        return this.origin.startDate();
    }

    @Override
    public Opt<Date> endDate() throws BazisException {
        return this.origin.endDate();
    }

    @Override
    public String payments() {
        return this.origin.payments();
    }

    @Override
    public JsonElement asJson() throws BazisException {
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
        json.addProperty(JsonAppoint.PAYMENTS, this.payments());
        return json;
    }

    @SuppressWarnings("MethodMayBeStatic")
    private String dateAsText(Opt<Date> date) {
        //noinspection ReturnOfNull
        return date.has() ? JsonAppoint.DATE_FORMAT.format(date.get()) : null;
    }

    private static final class Parsed implements Appoint {

        private final JsonElement json;

        private Parsed(JsonElement json) {
            this.json = json;
        }

        @Override
        public String type() {
            return this.string(JsonAppoint.TYPE);
        }

        @Override
        public String msp() {
            return this.string(JsonAppoint.MSP);
        }

        @Override
        public String category() {
            return this.string(JsonAppoint.CATEGORY);
        }

        @Override
        public String child() {
            return this.string(JsonAppoint.CHILD);
        }

        @Override
        public String status() {
            return this.string(JsonAppoint.STATUS);
        }

        @Override
        public Opt<Date> startDate() throws BazisException {
            return this.date(JsonAppoint.START_DATE);
        }

        @Override
        public Opt<Date> endDate() throws BazisException {
            return this.date(JsonAppoint.END_DATE);
        }

        @Override
        @SuppressWarnings({
            "HardcodedLineSeparator", "DynamicRegexReplaceableByCompiledPattern"
        })
        public String payments() {
            return this.string(JsonAppoint.PAYMENTS)
                .replace(", ", "\n").trim();
        }

        private String string(String property) {
            return this.json.getAsJsonObject().get(property).getAsString();
        }

        private Opt<Date> date(String property) throws BazisException {
            try {
                final JsonElement element =
                    this.json.getAsJsonObject().get(property);
                return element == null
                    ? new EmptyOpt<Date>()
                    : new OptOf<>(
                        JsonAppoint.DATE_FORMAT.parse(element.getAsString())
                    );
            } catch (final ParseException ex) {
                throw new BazisException(ex);
            }
        }

    }

}
