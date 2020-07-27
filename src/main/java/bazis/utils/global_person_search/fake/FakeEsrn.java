package bazis.utils.global_person_search.fake;

import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import java.util.Collections;
import java.util.Map;

public final class FakeEsrn implements Esrn {

    @Override
    public Person person(Number id) {
        return new FakePerson();
    }

    @Override
    public Map<String, String> measures(String links) {
        return Collections.singletonMap(
            "37145780-704c-48a2-9272-1f99afddaa9f",
            "Ежемесячная денежная компенсация военнослужащим"
        );
    }

}
