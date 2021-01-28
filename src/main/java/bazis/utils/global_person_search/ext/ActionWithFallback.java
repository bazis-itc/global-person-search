package bazis.utils.global_person_search.ext;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmRequest;
import sx.common.SXUtils;

public final class ActionWithFallback implements SitexAction {

    private final SitexAction origin;

    private final SitexAction fallback;

    public ActionWithFallback(SitexAction origin, String jsp) {
        this(origin, new JspAction(jsp));
    }

    private ActionWithFallback(SitexAction origin, SitexAction fallback) {
        this.origin = origin;
        this.fallback = fallback;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        //noinspection OverlyBroadCatchBlock
        try {
            this.origin.execute(request);
        } catch (final Throwable ex) {
            request.set(
                "error",
                ex.getMessage() == null ? ex.toString() : ex.getMessage()
            );
            request.set("stacktrace", SXUtils.getStackTrace(ex));
            this.fallback.execute(request);
        }
    }

}
