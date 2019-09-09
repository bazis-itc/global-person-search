package bazis.utils.global_person_search.json;

import bazis.utils.global_person_search.Person;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.Iterator;

public final class JsonPersons implements Iterable<Person>, Jsonable {

    private final Iterable<Person> origin;

    public JsonPersons(Iterable<Person> origin) {
        this.origin = origin;
    }

    @Override
    public Iterator<Person> iterator() {
        return this.origin.iterator();
    }

    @Override
    public JsonElement asJson() {
        final JsonArray json = new JsonArray();
        for (final Person person : this)
            json.add(new JsonPerson(person).asJson());
        return json;
    }

}
