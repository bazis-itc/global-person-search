package bazis.utils.global_person_search.fake;

import java.util.Map;
import sx.admin.AdmRequest;

@SuppressWarnings("MethodDoesntCallSuperMethod")
public final class FakeRequest extends AdmRequest {

    private final Map<String, String> params;

    public FakeRequest(Map<String, String> params) {
        super();
        this.params = params;
    }

    @Override
    public String getParam(String name) {
        if (!this.params.containsKey(name))
            throw new IllegalArgumentException("Param value not defined");
        return this.params.get(name);
    }

    @Override
    public void set(String key, Object val) {
        //nothing
    }

}
