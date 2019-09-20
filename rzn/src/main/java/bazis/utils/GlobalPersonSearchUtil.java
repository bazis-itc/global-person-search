package bazis.utils;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.BaseSearchUtil;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.Jsonable;
import com.google.gson.JsonObject;

public final class GlobalPersonSearchUtil extends BaseSearchUtil {

    public GlobalPersonSearchUtil() {
        super(
            "http://192.168.120.108:8080/central_test/",
            new Func<Person, Jsonable>() {
                @Override
                public Jsonable apply(Person person) throws BazisException {
                    return new JsonRequest(new JsonObject())
                        .withFio(person.fio())
                        .withBirthdate(person.birthdate())
                        .withSnils(person.snils());
                }
            }
        );
    }

}
