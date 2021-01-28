package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Opt;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.cactoos3.scalar.ScalarOf;
import bazis.cactoos3.text.CheckedText;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("OverloadedMethodsWithSameNumberOfParameters")
public interface ReportData extends Map<String, Object> {

    ReportData withString(String param, String str) throws BazisException;

    ReportData withString(String param, Text text) throws BazisException;

    ReportData withDate(String name, Date date) throws BazisException;

    ReportData withInt(String name, Number number) throws BazisException;

    ReportData withDouble(String name, Number number) throws BazisException;

    final class Immutable extends MapEnvelope<String, Object>
        implements ReportData {

        public Immutable() {
            this(new EmptyMap<String, Object>());
        }

        private Immutable(Map<String, Object> map) {
            super(new ScalarOf<>(map));
        }

        @Override
        public ReportData withString(String param, String str)
            throws BazisException {
            return this.with(
                param, str.isEmpty() ? new EmptyOpt<>() : new OptOf<>(str)
            );
        }

        @Override
        public ReportData withString(String param, Text text)
            throws BazisException {
            return this.withString(param, new CheckedText(text).asString());
        }

        @Override
        public ReportData withDate(String param, Date date)
            throws BazisException {
            return this.with(param, new OptOf<>(date));
        }

        @Override
        public ReportData withInt(String param, Number number)
            throws BazisException {
            return this.with(
                param,
                Double.isNaN(number.doubleValue())
                    ? new EmptyOpt<>() : new OptOf<>(number.intValue())
            );
        }

        @Override
        public ReportData withDouble(String param, Number number)
            throws BazisException {
            return this.with(
                param,
                Double.isNaN(number.doubleValue())
                    ? new EmptyOpt<>() : new OptOf<>(number.doubleValue())
            );
        }

        private ReportData with(String param, Opt<?> value)
            throws BazisException {
            if (this.containsKey(param)) throw new BazisException(
                String.format(
                    "Value of param '%s' already defined in map", param
                )
            );
            final ReportData result;
            if (value.has()) {
                final Map<String, Object> map = new HashMap<>(this);
                map.put(param, value.get());
                result = new ReportData.Immutable(
                    Collections.unmodifiableMap(map)
                );
            } else result = this;
            return result;
        }

    }

    final class Mutable extends MapEnvelope<String, Object>
        implements ReportData {

        public Mutable() {
            super(
                new ScalarOf<Map<String, Object>>(
                    new HashMap<String, Object>(0)
                )
            );
        }

        @Override
        public ReportData withString(String param, String str)
            throws BazisException {
            return this.with(
                param, str.isEmpty() ? new EmptyOpt<>() : new OptOf<>(str)
            );
        }

        @Override
        public ReportData withString(String param, Text text)
            throws BazisException {
            return this.withString(param, new CheckedText(text).asString());
        }

        @Override
        public ReportData withDate(String param, Date date)
            throws BazisException {
            return this.with(param, new OptOf<>(date));
        }

        @Override
        public ReportData withInt(String param, Number number)
            throws BazisException {
            return this.with(
                param,
                Double.isNaN(number.doubleValue())
                    ? new EmptyOpt<>() : new OptOf<>(number.intValue())
            );
        }

        @Override
        public ReportData withDouble(String param, Number number)
            throws BazisException {
            return this.with(
                param,
                Double.isNaN(number.doubleValue())
                    ? new EmptyOpt<>() : new OptOf<>(number.doubleValue())
            );
        }

        private ReportData with(String param, Opt<?> value)
            throws BazisException {
            if (this.containsKey(param)) throw new BazisException(
                String.format(
                    "Value of param '%s' already defined in map", param
                )
            );
            if (value.has()) this.put(param, value.get());
            return this;
        }

    }

}
