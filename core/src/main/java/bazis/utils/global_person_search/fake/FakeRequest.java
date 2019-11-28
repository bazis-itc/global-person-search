package bazis.utils.global_person_search.fake;

import java.util.HashMap;
import java.util.Map;
import sx.admin.AdmRequest;

@SuppressWarnings("MethodDoesntCallSuperMethod")
public final class FakeRequest extends AdmRequest {

    private final Map<String, Object> map = new HashMap<>(0);

    @Override
    public void set(String key, Object value) {
        this.map.put(key, value);
    }

    @Override
    public Object get(String key) {
        return this.map.get(key);
    }

}
