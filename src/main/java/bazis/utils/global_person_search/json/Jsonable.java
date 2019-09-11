package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import com.google.gson.JsonElement;

public interface Jsonable {

    JsonElement asJson() throws BazisException;

}
