package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.cactoos3.exception.BazisException;
import java.util.HashMap;
import javax.servlet.http.HttpSession;

public final class FakeOperation extends UIOperationBase {

    static {
        UIOperationFactory.registory(
            "fake_operation", FakeOperation.class
        );
    }

    @Override
    public HashMap<String, String> exec(HashMap<String, String> inputMap,
        String requestId, HttpSession session) throws Exception {
        throw new BazisException("Ivanov exception message");
    }

}
