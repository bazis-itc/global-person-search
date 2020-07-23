package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.ParamsOf;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.PrintedPayouts;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.dates.Period;
import bazis.utils.global_person_search.dates.SqlDate;
import bazis.utils.global_person_search.ext.SetOf;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import sx.admin.AdmRequest;

public final class DocProtocol implements Protocol {

    private static final String NO_RESULT =
        "Сведения о гражданине на оправшиваемых районах отсутствуют";

    private final DSLContext context;

    private final Esrn esrn;

    private final Iterable<Iterable<Person>> lists;

    public DocProtocol(DSLContext context, Esrn esrn) {
        this(context, esrn, new EmptyIterable<Iterable<Person>>());
    }

    private DocProtocol(DSLContext context, Esrn esrn,
        Iterable<Iterable<Person>> lists) {
        this.context = context;
        this.esrn = esrn;
        this.lists = lists;
    }

    @Override
    public Protocol append(Iterable<Person> persons) {
        return new DocProtocol(
            this.context, this.esrn,
            new JoinedIterable<>(this.lists, new IterableOf<>(persons))
        );
    }

    @Override
    public void outputTo(AdmRequest request, Map<String, Object> params)
        throws BazisException {
        final int docId = this.createDoc(request);
        System.out.println(docId);
        this.createDetail(docId, new ListOf<>(this.lists).get(0), true);
        this.createDetail(docId, new ListOf<>(this.lists).get(1), false);
    }

    private Integer createDoc(AdmRequest request) throws BazisException {
        final Field<Integer> identity = DSL.field("OUID", Integer.class);
        return this.context
            .insertInto(DSL.table("WM_ACTDOCUMENTS"))
            .set(
                DSL.field("A_STATUS"),
                DSL.field(
                    "(SELECT A_ID FROM ESRN_SERV_STATUS WHERE A_STATUSCODE = 'act')",
                    Integer.class
                )
            )
            .set(
                DSL.field("A_CREATEDATE"),
                DSL.field("GETDATE()", Double.class)
            )
            .set(
                DSL.field("TS"), DSL.field("GETDATE()", Double.class)
            )
            .set(
                DSL.field("GUID"), DSL.field("NEWID()", Double.class)
            )
            .set(
                DSL.field("A_DOCSTATUS"),
                DSL.field(
                    "(SELECT A_OUID FROM SPR_DOC_STATUS WHERE A_CODE = 'active')",
                    Integer.class
                )
            )
            .set(
                DSL.field("DOCUMENTSTYPE"),
                DSL.field(
                    "(SELECT A_ID FROM PPR_DOC WHERE A_CODE = 'globalPersonSearchDoc')",
                    Integer.class
                )
            )
            .set(
                DSL.field("ISSUEEXTENSIONSDATE"),
                DSL.field("GETDATE()", Double.class)
            )
            .set(
                DSL.field("PERSONOUID", Integer.class),
                new ParamsOf(request).objId().intValue()
            )
            .set(
                DSL.field("A_WHITE_LIST", String.class),
                new CheckedText(
                    new JoinedText(
                        ", ",
                        new SetOf<>(
                            new MappedIterable<>(
                                new JoinedIterable<>(this.lists),
                                new Func<Person, String>() {
                                    @Override
                                    public String apply(Person pers)
                                        throws BazisException {
                                        return pers.borough();
                                    }
                                }
                            )
                        )
                    )
                ).asString()
            )
            .set(
                DSL.field("A_BLACK_LIST", String.class),
                request.getParam("fails")
            )
            .set(
                DSL.field("A_START_DATE", Double.class),
                new SqlDate(new ParamsOf(request).startDate()).doubleValue()
            )
            .set(
                DSL.field("A_END_DATE", Double.class),
                new SqlDate(new ParamsOf(request).endDate()).doubleValue()
            )
            .set(
                DSL.field("A_REQUESTED_MSP", String.class),
                new CheckedText(
                    new JoinedText(
                        ", ",
                        this.esrn.measures(new ParamsOf(request).msp()).values()
                    )
                ).asString()
            )
            .set(
                DSL.field("A_NOTE", String.class),
                new ListOf<>(new JoinedIterable<>(this.lists)).isEmpty()
                    ? DocProtocol.NO_RESULT : null
            )
            .returning(identity)
            .fetchOne().getValue(identity);
    }

    private void createDetail(Integer docId, Iterable<Person> persons,
        boolean primary) throws BazisException {
        for (final Person person : persons)
            for (final Appoint appoint : person.appoints()) this.context
                .insertInto(DSL.table("GLOBAL_PERSON_SEARCH_APPOINT"))
                .set(DSL.field("A_DOC", Integer.class), docId)
                .set(DSL.field("A_BOROUGH", String.class), person.borough())
                .set(DSL.field("A_MSP", String.class), appoint.msp())
                .set(DSL.field("A_CATEGORY", String.class), appoint.category())
                .set(DSL.field("A_CHILD", String.class), appoint.child())
                .set(
                    DSL.field("A_APPOINT_STATUS", String.class),
                    appoint.status()
                )
                .set(
                    DSL.field("A_PERIOD", String.class),
                    new Period(
                        " ", appoint.startDate(), appoint.endDate()
                    ).asString()
                )
                .set(
                    DSL.field("A_PAYOUTS", String.class),
                    new PrintedPayouts(appoint.payouts()).asString()
                )
                .set(DSL.field("A_FULL_IDENTITY", Boolean.class), primary)
                .execute();
    }

}
