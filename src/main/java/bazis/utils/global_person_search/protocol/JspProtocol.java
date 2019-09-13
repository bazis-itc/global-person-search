package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.jsp.JspPerson;
import java.util.Collection;
import java.util.LinkedList;
import sx.common.SXRequest;

public final class JspProtocol implements Protocol {

    private final SXRequest request;

    private final String param;

    public JspProtocol(SXRequest request, String param) {
        this.request = request;
        this.param = param;
    }

    @Override
    public void write(Person person) throws BazisException {
        if (this.request.get(this.param) == null)
            this.request.set(this.param, new LinkedList<JspPerson>());
        final Collection<JspPerson> list =
            (Collection<JspPerson>) this.request.get(this.param);
        list.add(new JspPerson(person));
    }

}
