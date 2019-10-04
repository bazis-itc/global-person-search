package bazis.utils.global_person_search.ext;

import bazis.cactoos3.exception.BazisException;
import java.util.Map;
import sx.admin.AdmRequest;

public final class DispatchAction implements SitexAction {

    private final SitexAction def;

    private final Map<String, SitexAction> map;

    public DispatchAction(SitexAction def, Map<String, SitexAction> map) {
        this.def = def;
        this.map = map;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        final String cmd = request.getParam("cmd");
        final SitexAction action;
        if (cmd == null) action = this.def;
        else if (this.map.containsKey(cmd)) action = this.map.get(cmd);
        else throw new BazisException(String.format("Unknown cmd '%s'", cmd));
        action.execute(request);
    }

}
