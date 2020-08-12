package bazis.utils.global_person_search.fake;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Report;
import java.io.File;
import java.util.Map;

public final class FakeReport implements Report {

    private final Number group;

    private final Map<String, Object> output;

    public FakeReport(Number group, Map<String, Object> output) {
        this.group = group;
        this.output = output;
    }

    @Override
    public Report append(Number grp, Map<String, Object> row) {
        if (this.group.intValue() == grp.intValue()) {
            this.output.clear();
            this.output.putAll(row);
        }
        return this;
    }

    @Override
    public File create(Map<String, Object> params) throws BazisException {
        return new File("");
    }

}
