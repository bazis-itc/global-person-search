package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.utils.global_person_search.EncryptedText;
import bazis.utils.global_person_search.jdbc.JdbcRegister;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.uson.UsonBoroughs;
import java.sql.Connection;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import sx.bazis.uninfoobmen.sys.store.DataObject;
import sx.bazis.uninfoobmen.sys.store.ReturnDataObject;
import sx.datastore.SXDsFactory;

public final class GlobalPersonSearchOperation extends UIOperationBase {

    static {
        //noinspection deprecation
        UIOperationFactory.registory(
            "global_person_search", GlobalPersonSearchOperation.class
        );
    }

    @Override
    public ReturnDataObject exec(HashMap<String, String> hashMap,
        DataObject dataObject, HttpSession httpSession) {
        //noinspection OverlyBroadCatchBlock
        try (final Connection conn = SXDsFactory.getDs().getConnection()) {
            final JsonRequest request = new JsonRequest(
                new JsonText(
                    new EncryptedText(dataObject.getInputStream())
                ).asJson()
            );
            return super.getReturnMessage(
                "COMPLETE", "", null,
                new EncryptedText(
                    new JsonText(
                        new JsonPersons(
                            new JdbcRegister(conn, new UsonBoroughs(conn))
                                .persons(
                                    request.fio(),
                                    request.birthdate(),
                                    request.snils()
                                )
                        )
                    )
                ).asBytes()
            );
        } catch (final Exception ex) {
            return super.getReturnMessage(
                "ERROR",
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                ex
            );
        }
    }

}
