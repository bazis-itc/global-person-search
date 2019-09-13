package bazis.utils;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.Report;
import bazis.utils.global_person_search.fake.FakePerson;
import bazis.utils.global_person_search.protocol.CompositeProtocol;
import bazis.utils.global_person_search.protocol.ForkProtocol;
import bazis.utils.global_person_search.protocol.JspProtocol;
import bazis.utils.global_person_search.protocol.RtfProtocol;
import bazis.utils.global_person_search.sx.SxPerson;
import bazis.utils.global_person_search.sx.SxReport;
import sx.admin.AdmAction;
import sx.admin.AdmApplication;
import sx.admin.AdmRequest;
import sx.cms.CmsActionUtils;
import sx.datastore.SXDsFactory;
import sx.datastore.SXId;
import sx.datastore.impl.fs.SXDsFs;

//bazis.utils.GlobalPersonSearchUtil
public final class GlobalPersonSearchUtil extends AdmAction {

    @Deprecated
    private static final String SNILS_ATTR = "snils";

    @Override
    public void execute(
        AdmRequest request, AdmApplication app) throws Exception {
        final String cmd = request.getParam("cmd");
        if (cmd == null) super.includeTemplate(
            "global_person_search/openwindow", request
        );
        else if (cmd.equals("openWindowCmd")) {
            final Person person = new SxPerson(
                new SXId(request.getAction().getObjId())
            );
            final Iterable<Person> persons =
                new IterableOf<Person>(new FakePerson(), new FakePerson());
//            final Iterable<Person> persons =
//                new JsonPersons(
//                    new JsonParser().parse(
//                        new Server(
//                            "http://10.65.12.11:8080/update_test_central/"
//                        ).send(person.snils())
//                    ).getAsJsonArray()
//                );
            if (new IsEmpty(persons).value()) request.set(
                "error", "Нет информации о данном гражданине на других базах"
            );
            final Report report =
                new SxReport("globalPersonSearchProtocol");
            final Protocol protocol = new ForkProtocol(
                person.fio(), person.birthdate(),
                new CompositeProtocol(
                    new JspProtocol(request, "completely"),
                    new RtfProtocol(report, 1)
                ),
                new CompositeProtocol(
                    new JspProtocol(request, "partially"),
                    new RtfProtocol(report, 2)
                )
            );
            for (final Person prs : persons) protocol.write(prs);
            request.set(
                "protocol",
                CmsActionUtils.getDownloadURL(
                    SXDsFs.class.cast(SXDsFactory.getDs("reports"))
                        .file2Obj(
                            report.create(
                                new Report.Data()
                                    .withDate("startDate", null)
                                    .withDate("endDate", null)
                                    .withString("mspList", "")
                            )
                        ).getId()
                )
            );
            super.includeTemplate(
                "global_person_search/result", request
            );
        }
        else throw new BazisException("Unknown cmd");
    }

}
