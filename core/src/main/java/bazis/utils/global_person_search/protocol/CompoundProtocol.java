package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import java.util.Map;
import sx.admin.AdmRequest;

public final class CompoundProtocol implements Protocol {

    private final Protocol first, second;

    public CompoundProtocol(Protocol first, Protocol second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Protocol append(Iterable<Person> persons) throws BazisException {
        return new CompoundProtocol(
            this.first.append(persons),
            this.second.append(persons)
        );
    }

    @Override
    public void outputTo(AdmRequest request,
        Map<String, Object> params) throws BazisException {
        this.first.outputTo(request, params);
        this.second.outputTo(request, params);
    }

}
