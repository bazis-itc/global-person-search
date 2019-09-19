package bazis.utils.global_person_search.sx;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Report;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import sx.admin.actions.util.CreateReport;
import sx.common.SXSession;
import sx.datastore.SXId;
import sx.datastore.SXObj;
import sx.datastore.impl.sitex2.beans.report.SXReport;
import sx.datastore.meta.SXClass;
import sx.datastore.params.SXObjListParams;
import sx.sec.SXLogin;

public final class SxReport implements Report {

    private final String code;

    private final Map<Integer, List<Map<String, Object>>> data;

    public SxReport(String code) {
        this.code = code;
        this.data = new HashMap<>(0);
    }

    @Override
    public Report append(Number group, Map<String, Object> row) {
        if (!this.data.containsKey(group.intValue()))
            this.data.put(
                group.intValue(),
                new LinkedList<Map<String, Object>>()
            );
        this.data.get(group.intValue()).add(row);
        return this;
    }

    @Override
    public File create(Map<String, Object> params) throws BazisException {
        final SXReport report;
        final SXLogin login = SXSession.getSXSession().getLogin();
        final SXId id = this.id();
        try {
            report = new CreateReport().create(
                id, null, login, params, this.data
            );
        } catch (final Exception ex) {
            throw new BazisException(
                String.format(
                    "Failed to create report \"%s\"", id.toString()
                ),
                ex
            );
        }
        return report.getReportFile();
    }

    private SXId id() throws BazisException {
        final SXObj report;
        try {
            report = new SXObjListParams("SXReport")
                .setDefaultAttrCollection(SXClass.NONE_ATTRS)
                .addCondition("code", this.code)
                .getObj();
        } catch (final Exception ex) {
            throw new BazisException(ex);
        }
        if (report == null) throw new BazisException(
            String.format("Report '%s' not found", this.code)
        );
        return report.getId();
    }

}
