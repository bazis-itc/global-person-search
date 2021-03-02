package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import java.util.Map;
import sx.admin.AdmRequest;

public final class RtfProtocol implements Protocol {

    private final Scalar<Protocol> scalar;

    public RtfProtocol(final Esrn esrn, final Map<String, String> config) {
        this.scalar = new Scalar<Protocol>() {
            @Override
            public Protocol value() {
                return Boolean.parseBoolean(config.get("displayPetitions"))
                    ? new ZipProtocol(esrn) : new SimpleProtocol(esrn);
            }
        };
    }

    @Override
    public Protocol append(Iterable<Person> persons) throws BazisException {
        return new CheckedScalar<>(this.scalar).value().append(persons);
    }

    @Override
    public void outputTo(AdmRequest request) throws BazisException {
        new CheckedScalar<>(this.scalar).value().outputTo(request);
    }

}
