package bazis.utils.global_person_search.json;

import bazis.cactoos3.exception.BazisException;
import com.google.gson.JsonObject;
import java.util.Date;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import sx.common.DateUtils;

public final class JsonRequestTest {

    @Test
    public void test() throws BazisException {
        final String
            fio = "Иванов Иван Иванович",
            snils = "123-456-789-00";
        final Date birthdate = DateUtils.getBeginDate(new Date());
        final JsonRequest request = new JsonRequest(
            new JsonRequest(new JsonObject())
                .withFio(fio)
                .withBirthdate(birthdate)
                .withSnils(snils)
                .asJson()
        );
        MatcherAssert.assertThat("fio", request.fio(), Matchers.equalTo(fio));
        MatcherAssert.assertThat(
            "birthdate", request.birthdate().get(), Matchers.equalTo(birthdate)
        );
        MatcherAssert.assertThat(
            "snils", request.snils(), Matchers.equalTo(snils)
        );
    }

}