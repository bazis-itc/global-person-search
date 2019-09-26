package bazis.utils.global_person_search.action;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmRequest;

public interface Action {

    void execute(AdmRequest request) throws BazisException;

    Action NONE = new Action() {
        @Override
        public void execute(AdmRequest request) {
            //nothing
        }
    };

}
