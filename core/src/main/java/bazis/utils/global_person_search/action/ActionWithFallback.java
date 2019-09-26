package bazis.utils.global_person_search.action;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmRequest;
import sx.common.SXUtils;

public final class ActionWithFallback implements Action {

    private final Action origin;

    private final Action fallback;

    public ActionWithFallback(Action origin, String jsp) {
        this(origin, new JspAction(jsp));
    }

    public ActionWithFallback(Action origin, Action fallback) {
        this.origin = origin;
        this.fallback = fallback;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        try {
            this.origin.execute(request);
        } catch (final Exception ex) {
            request.set(
                "error",
                ex.getMessage() == null ? ex.toString() : ex.getMessage()
            );
            request.set("stacktrace", SXUtils.getStackTrace(ex));
            this.fallback.execute(request);
        }
    }

}
