package bazis.utils.global_person_search.ext;

import bazis.cactoos3.exception.BazisException;
import sx.admin.AdmRequest;

public interface SitexAction {

    void execute(AdmRequest request) throws BazisException;

    @SuppressWarnings("ConstantDeclaredInInterface")
    SitexAction NONE = new SitexAction() {
        @Override
        public void execute(AdmRequest request) {
            //nothing
        }
    };

}
