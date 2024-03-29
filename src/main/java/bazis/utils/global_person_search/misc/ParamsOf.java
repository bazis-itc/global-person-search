package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.opt.EmptyOpt;
import bazis.cactoos3.opt.OptOf;
import bazis.utils.global_person_search.dates.IsoDate;
import java.util.Date;
import sx.admin.AdmRequest;
import sx.common.DateUtils;

public final class ParamsOf {

    private final AdmRequest request;

    public ParamsOf(AdmRequest request) {
        this.request = request;
    }

    @Deprecated
    public Number objId() {
        return Integer.parseInt(
            this.request.getParam("objId").split("@")[0]
        );
    }

    public Opt<Number> personId() {
        final String[] parts =
            this.request.getParam("objId").split("@");
        return parts.length == 2 && "wmPersonalCard".equals(parts[1])
            ? new OptOf<Number>(Integer.parseInt(parts[0]))
            : new EmptyOpt<Number>();
    }

    public Date startDate() throws BazisException {
        return this.dateFrom("yearOfStart", "monthOfStart");
    }

    public Date endDate() throws BazisException {
        return DateUtils.getMonthYearEndDate(
            this.dateFrom("yearOfEnd", "monthOfEnd")
        );
    }

    public String msp() {
        return "on".equals(this.request.getParam("isAllMsp"))
            ? "" : this.request.getParam("data(mspList)");
    }

    private Date dateFrom(String year, String month)
        throws BazisException {
        return new IsoDate(
            String.format(
                "%s-%s-01",
                this.request.getParam(year),
                this.request.getParam(month)
            )
        ).value();
    }

}
