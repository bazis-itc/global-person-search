package bazis.utils.global_person_search.action;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmAction;
import sx.admin.AdmRequest;

public final class JspAction implements Action {

    private final Action origin;

    private final String jsp;

    public JspAction(String jsp) {
        this(Action.NONE, jsp);
    }

    public JspAction(Action origin, String jsp) {
        this.origin = origin;
        this.jsp = jsp;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        this.origin.execute(request);
        try {
            AdmAction.class.getDeclaredMethod(
                "includeTemplate", String.class, AdmRequest.class
            ).invoke(request.getAction(), this.jsp, request);
        } catch (final ReflectiveOperationException ex) {
            throw new BazisException(ex);
        }
    }

}
