package bazis.utils.global_person_search.json;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.dates.IsoDate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

@SuppressWarnings({"CyclicClassDependency", "ClassWithTooManyMethods"})
final class JsonAppoint implements Appoint, Jsonable {

    private static final String
        TYPE = "type", MSP = "msp", CATEGORY = "category",
        CHILD = "child", STATUS = "status",
        START_DATE = "startDate", END_DATE = "endDate",
        PAYMENTS = "payments", PAYOUTS = "payouts";

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
    public Iterable<Payout> payouts() {
        return this.origin.payouts();
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
        final JsonArray payouts = new JsonArray();
        for (final Payout payout : this.payouts())
            payouts.add(new JsonPayout(payout).asJson());
        json.add(JsonAppoint.PAYOUTS, payouts);
        return json;
    }

    @SuppressWarnings("MethodMayBeStatic")
    private String dateAsText(Opt<Date> date) throws BazisException {
        //noinspection ReturnOfNull
        return date.has() ? new IsoDate(date.get()).asString() : null;
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
        public Iterable<Payout> payouts() {
            return new MappedIterable<>(
                this.json
                    .getAsJsonObject()
                    .get(JsonAppoint.PAYOUTS)
                    .getAsJsonArray(),
                new Func<JsonElement, Payout>() {
                    @Override
                    public Payout apply(JsonElement item) {
                        return new JsonPayout(item);
                    }
                }
            );
        }

        private String string(String property) {
            return this.json.getAsJsonObject().get(property).getAsString();
        }

        private Opt<Date> date(String property) throws BazisException {
            final JsonElement element =
                this.json.getAsJsonObject().get(property);
            return element == null
                ? new EmptyOpt<Date>()
                : new OptOf<>(new IsoDate(element.getAsString()).value());
        }

    }

}
