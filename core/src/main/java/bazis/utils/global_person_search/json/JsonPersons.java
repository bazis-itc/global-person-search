package bazis.utils.global_person_search.json;

import bazis.cactoos3.Func;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Person;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.Iterator;

public final class JsonPersons implements Iterable<Person>, Jsonable {

    private final Iterable<Person> origin;

    public JsonPersons(JsonElement json) {
        this(new JsonPersons.Parsed(json));
    }

    public JsonPersons(Iterable<Person> origin) {
        this.origin = origin;
    }

    @Override
    public Iterator<Person> iterator() {
        return this.origin.iterator();
    }

    @Override
    public JsonElement asJson() throws BazisException {
        final JsonArray json = new JsonArray();
        for (final Person person : this)
            json.add(new JsonPerson(person).asJson());
        return json;
    }

    private static final class Parsed implements Iterable<Person> {

        private final JsonElement json;

        private Parsed(JsonElement json) {
            this.json = json;
        }

        @Override
        public Iterator<Person> iterator() {
            return new MappedIterable<>(
                this.json.getAsJsonArray(),
                new Func<JsonElement, Person>() {
                    @Override
                    public Person apply(JsonElement person) {
                        return new JsonPerson(person.getAsJsonObject());
                    }
                }
            ).iterator();
        }

    }

}
