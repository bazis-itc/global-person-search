package bazis.utils.global_person_search.fake;

import bazis.cactoos3.exception.BazisException;
import bazis.sitex3.SitexReport;
import java.io.File;
import java.util.Map;

public final class FakeReport implements SitexReport {

    private final Number group;

    private final Map<String, Object> output;

    public FakeReport(Number group, Map<String, Object> output) {
        this.group = group;
        this.output = output;
    }

    @Override
    public SitexReport append(Number grp, Map<String, Object> row) {
        if (this.group.intValue() == grp.intValue()) {
            this.output.clear();
            this.output.putAll(row);
        }
        return this;
    }

    @Override
    public File toFile(Map<String, Object> params) throws BazisException {
        return new File("");
    }

}
