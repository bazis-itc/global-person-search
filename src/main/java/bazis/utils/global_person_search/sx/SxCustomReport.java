package bazis.utils.global_person_search.sx;

import bazis.cactoos3.exception.BazisException;
import bazis.sitex3.SitexReport;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import sx.common.SXSession;
import sx.common.reportsystem.ReportFactory;
import sx.datastore.SXDsFactory;
import sx.datastore.SXId;
import sx.datastore.SXObj;
import sx.datastore.impl.fs.SXDsFs;
import sx.datastore.impl.sitex2.beans.report.SXReport;
import sx.datastore.meta.SXClass;
import sx.datastore.params.SXObjListParams;

public final class SxCustomReport implements SitexReport {

    private final String code;

    private final File template;

    private final Map<String, List<Map<String, Object>>> data;

    public SxCustomReport(String code, File template) {
        this.code = code;
        this.template = template;
        this.data = new HashMap<>(0);
    }

    @Override
    public SitexReport append(Number group, Map<String, Object> row) {
        final String key = Integer.toString(group.intValue());
        if (!this.data.containsKey(key))
            this.data.put(key, new LinkedList<Map<String, Object>>());
        this.data.get(key).add(new HashMap<>(row));
        return this;
    }

    @Override
    public File toFile(Map<String, Object> params) throws BazisException {
        final SXId id = this.id();
        try {
            final SXReport sxReport =
                (SXReport) new SXObjListParams(id)
                    .setUseCache(false)
                    .addSelectedAttr(SXClass.IN_FORM_ATTRS)
                    .getObj();
            sxReport.setAttr(
                "repFile",
                SXDsFs.class.cast(SXDsFactory.getDs("reports"))
                    .file2Obj(this.template).getId()
            );
            sxReport.setLogin(SXSession.getSXSession().getLogin());
            sxReport.setParams(params);
            sxReport.setQueryValue(this.data);
            ReportFactory.create(sxReport);
            return sxReport.getReportFile();
        } catch (final Exception ex) {
            throw new BazisException(
                String.format(
                    "Failed to create report '%s'", id.toString()
                ),
                ex
            );
        }
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
