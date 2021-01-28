package bazis.utils.global_person_search.action;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.fake.FakeEsrn;
import bazis.utils.global_person_search.fake.FakeRequest;
import org.jooq.impl.DSL;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public final class CreateDocITCase {

    @Test
    public void test() throws BazisException {
        new CreateDoc(
            DSL.using(//http://10.1.13.1:8080/
                "jdbc:sqlserver://192.168.11.2;databaseName=kadosh",
                "sa", "sitex"
            ),
            new FakeEsrn()
        ).execute(new FakeRequest());
    }

}