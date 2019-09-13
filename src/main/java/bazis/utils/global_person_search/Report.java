package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.scalar.ScalarOf;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Report {

    Report append(Number group, Map<String, Object> row);

    File create(Map<String, Object> params) throws BazisException;

    final class Data extends MapEnvelope<String, Object> {

        public Data() {
            super(
                new ScalarOf<Map<String, Object>>(
                    new HashMap<String, Object>(0)
                )
            );
        }

        public Report.Data withString(String param, String str) {
            if (!str.isEmpty()) this.put(param, str);
            return this;
        }

        public Report.Data withInt(String param, Number number) {
            if (!Double.isNaN(number.doubleValue()))
                this.put(param, number.intValue());
            return this;
        }

        public Report.Data withDouble(String param, Number number) {
            if (!Double.isNaN(number.doubleValue()))
                this.put(param, number.doubleValue());
            return this;
        }

        public Report.Data withDate(String param, Date date) {
            this.put(param, date);
            return this;
        }

    }

}
