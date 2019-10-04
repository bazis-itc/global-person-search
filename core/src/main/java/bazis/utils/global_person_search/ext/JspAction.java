package bazis.utils.global_person_search.ext;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmAction;
import sx.admin.AdmRequest;

public final class JspAction implements SitexAction {

    private final SitexAction origin;

    private final String jsp;

    public JspAction(String jsp) {
        this(SitexAction.NONE, jsp);
    }

    public JspAction(SitexAction origin, String jsp) {
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