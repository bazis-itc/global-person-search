package bazis.utils.global_person_search;

import bazis.cactoos3.Func;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.Entry;
import bazis.cactoos3.map.MapOf;
import bazis.utils.global_person_search.ext.ActionWithFallback;
import bazis.utils.global_person_search.ext.DispatchAction;
import bazis.utils.global_person_search.ext.JspAction;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.Jsonable;
import com.google.gson.JsonObject;
import sx.admin.AdmAction;
import sx.admin.AdmApplication;
import sx.admin.AdmRequest;
import sx.bazis.uninfoobmen.sys.function.Function;

public abstract class BaseSearchUtil extends AdmAction {

    private final SitexAction action;

    protected BaseSearchUtil() {
        this(
            new Func<Person, Jsonable>() {
                @Override
                public Jsonable apply(Person person) throws BazisException {
                    return new JsonRequest(new JsonObject())
                        .withSnils(person.snils());
                }
            }
        );
    }

    @SuppressWarnings("HardcodedFileSeparator")
    protected BaseSearchUtil(Func<Person, Jsonable> requests) {
        this(
            new ActionWithFallback(
                new DispatchAction(
                    new JspAction("global_person_search/openwindow"),
                    new MapOf<>(
                        new Entry<String, SitexAction>(
                            "openWindowCmd",
                            new JspAction("global_person_search/params")
                        ),
                        new Entry<String, SitexAction>(
                            "paramsCmd",
                            new JspAction(
                                new ResultAction(
                                    new Text() {
                                        @Override
                                        public String asString() {
                                            return Function.getCentUrl();
                                        }
                                    },
                                    requests
                                ),
                                "global_person_search/result"
                            )
                        )
                    )
                ),
                "global_person_search/error"
            )
        );
    }

    private BaseSearchUtil(SitexAction action) {
        super();
        this.action = action;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final void execute(AdmRequest request, AdmApplication app)
        throws BazisException {
        this.action.execute(request);
    }

}
