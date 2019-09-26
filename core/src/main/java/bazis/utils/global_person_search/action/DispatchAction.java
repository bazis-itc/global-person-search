package bazis.utils.global_person_search.action;

import bazis.cactoos3.exception.BazisException;
import java.util.Map;
import sx.admin.AdmRequest;

public final class DispatchAction implements Action {

    private final Action def;

    private final Map<String, Action> map;

    public DispatchAction(Action def, Map<String, Action> map) {
        this.def = def;
        this.map = map;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        final String cmd = request.getParam("cmd");
        final Action action;
        if (cmd == null) action = this.def;
        else if (this.map.containsKey(cmd)) action = this.map.get(cmd);
        else throw new BazisException(String.format("Unknown cmd '%s'", cmd));
        action.execute(request);
    }

}
