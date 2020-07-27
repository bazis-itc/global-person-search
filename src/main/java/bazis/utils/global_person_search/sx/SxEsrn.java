package bazis.utils.global_person_search.sx;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import java.util.Map;
import sx.datastore.SXId;

public final class SxEsrn implements Esrn {

    @Override
    public Person person(final Number id) throws BazisException {
        return new CheckedScalar<>(
            new Scalar<Person>() {
                @Override
                public Person value() throws Exception {
                    return new SxPerson(
                        new SXId("wmPersonalCard", id.intValue())
                    );
                }
            }
        ).value();
    }

    @Override
    public Map<String, String> measures(String links) {
        return links.isEmpty()
            ? new EmptyMap<String, String>() : new MspMap(links);
    }

}
