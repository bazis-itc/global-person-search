package sx.bazis.uninfoobmen.web.bean.operation;

import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.jdbc.JdbcRegister;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonRequest;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.misc.EncryptedText;
import bazis.utils.global_person_search.misc.UsonBoroughs;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
        ReturnDataObject result;
        //noinspection OverlyBroadCatchBlock
        try (final Connection conn = SXDsFactory.getDs().getConnection()) {
            final JsonRequest request = new JsonRequest(
                new JsonText(
                    new EncryptedText(dataObject.getInputStream())
                ).asJson()
            );
            final Collection<String> fails = new LinkedList<>();
            final DataObject response = new EncryptedText(
                new JsonText(
                    new JsonPersons(
                        new JdbcRegister(
                            conn, new UsonBoroughs(conn, fails)
                        ).persons(
                            request.fio(),
                            request.birthdate(),
                            request.snils()
                        )
                    )
                )
            ).asBytes();
            result = super.getReturnMessage(
                "COMPLETE",
                new JoinedText(", ", fails).asString(),
                null, response
            );
        } catch (final Exception ex) {
            result = super.getReturnMessage(
                "ERROR",
                ex.getMessage() == null ? ex.toString() : ex.getMessage(),
                ex
            );
        }
        return result;
    }

}
