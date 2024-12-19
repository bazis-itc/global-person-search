package bazis.utils;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.utils.global_person_search.action.CreateDoc;
import bazis.utils.global_person_search.action.ResultAction;
import bazis.utils.global_person_search.ext.ActionWithFallback;
import bazis.utils.global_person_search.ext.DispatchAction;
import bazis.utils.global_person_search.ext.JspAction;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.misc.ParamsOf;
import bazis.utils.global_person_search.sx.SxEsrn;
import sx.admin.AdmAction;
import sx.admin.AdmApplication;
import sx.admin.AdmRequest;

public final class GlobalPersonSearchUtil extends AdmAction {

    private final SitexAction action;

    @SuppressWarnings("HardcodedFileSeparator")
    public GlobalPersonSearchUtil() {
        this(
            new ActionWithFallback(
                new DispatchAction(
                    new JspAction("global_person_search/openwindow"),
                    new MapOf<>(
                        new Entry<String, SitexAction>(
                            "openWindowCmd",
                            new JspAction(
                                new SitexAction() {
                                    @Override
                                    public void execute(AdmRequest request) {
                                        request.set(
                                            "extended",
                                            !new ParamsOf(request).personId().has()
                                        );
                                    }
                                },
                                "global_person_search/params"
                            )
                        ),
                        new Entry<String, SitexAction>(
                            "paramsCmd",
                            new JspAction(
                                new ResultAction(new SxEsrn()),
                                "global_person_search/result"
                            )
                        ),
                        new Entry<String, SitexAction>(
                            "resultCmd",
                            new JspAction(
                                new CreateDoc(new SxEsrn()),
                                "global_person_search/message"
                            )
                        )
                    )
                ),
                "global_person_search/error"
            )
        );
    }

    private GlobalPersonSearchUtil(SitexAction action) {
        super();
        this.action = action;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public void execute(AdmRequest request, AdmApplication app)
        throws BazisException {
        this.action.execute(request);
    }

}
