package bazis.utils.global_person_search;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.scalar.ScalarOf;
import bazis.utils.global_person_search.ext.SitexAction;
import bazis.utils.global_person_search.json.JsonPersons;
import bazis.utils.global_person_search.json.JsonText;
import bazis.utils.global_person_search.protocol.DocProtocol;
import bazis.utils.global_person_search.protocol.SplitProtocol;
import org.jooq.DSLContext;
import sx.admin.AdmRequest;

public final class CreateDoc implements SitexAction {

    private final CheckedScalar<DSLContext> context;

    private final Esrn esrn;

    public CreateDoc(DSLContext context, Esrn esrn) {
        this(new ScalarOf<>(context), esrn);
    }

    public CreateDoc(Scalar<DSLContext> context, Esrn esrn) {
        this.context = new CheckedScalar<>(new CachedScalar<>(context));
        this.esrn = esrn;
    }

    @Override
    public void execute(AdmRequest request) throws BazisException {
        new SplitProtocol(
            new DocProtocol(this.context.value(), this.esrn),
            this.esrn.person(new ParamsOf(request).objId())
        )
            .append(
                new RequestedPersons(
                    new JsonPersons(
                        new JsonText(request.getParam("persons")).asJson()
                    ),
                    this.esrn.measures(new ParamsOf(request).msp()).keySet(),
                    new ParamsOf(request).startDate(),
                    new ParamsOf(request).endDate()
                )
            )
            .outputTo(request, new EmptyMap<String, Object>());
        request.set("message", "Документ сформирован");
    }

}
