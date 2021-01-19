package bazis.utils.global_person_search.fake;

import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.ext.MapEntry;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonText;
import java.util.Map;
import sx.admin.AdmRequest;

@SuppressWarnings("MethodDoesntCallSuperMethod")
public final class FakeRequest extends AdmRequest {

    private final Map<String, String> params;

    public FakeRequest() {
        this(
            new MapOf<>(
                new MapEntry<>("objId", "10002@wmPersonalCard"),
                new MapEntry<>("yearOfStart", "2019"),
                new MapEntry<>("monthOfStart", "1"),
                new MapEntry<>("yearOfEnd", "2020"),
                new MapEntry<>("monthOfEnd", "2"),
                new MapEntry<>("isAllMsp", "on"),
                new MapEntry<>("data(mspList)", ""),
                new MapEntry<>("fails", "Неопрошенный район"),
                new MapEntry<>(
                    "persons",
                    new UncheckedText(
                        new JsonText(
                            new JsonPersons(
                                new IterableOf<Person>(
                                    new FakePerson(),
                                    new FakePerson(
                                        "Неиванов Владислав Александрович",
                                        new FakeAppoint()
                                    )
                                )
                            )
                        )
                    ).asString()
                )
            )
        );
    }

    public FakeRequest(Map<String, String> params) {
        super();
        this.params = params;
    }

    @Override
    public String getParam(String name) {
        if (!this.params.containsKey(name))
            throw new IllegalArgumentException("Param value not defined");
        return this.params.get(name);
    }

    @Override
    public Object get(String key) {
        return "";
    }

    @Override
    public void set(String key, Object val) {
        //nothing
    }

}
