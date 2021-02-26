package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.sitex3.SitexReport;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.ext.Entries;
import java.io.File;
import java.util.Map;
import sx.admin.AdmRequest;

public final class RtfProtocol implements Protocol {

    private final Esrn esrn;

    private final Iterable<Iterable<Person>> lists;

    public RtfProtocol(Esrn esrn) {
        this(esrn, new EmptyIterable<Iterable<Person>>());
    }

    private RtfProtocol(Esrn esrn, Iterable<Iterable<Person>> lists) {
        this.esrn = esrn;
        this.lists = lists;
    }

    @Override
    public Protocol append(Iterable<Person> persons) {
        return new RtfProtocol(
            this.esrn, new JoinedIterable<>(
                this.lists, new IterableOf<>(persons)
            )
        );
    }

    @Override
    public void outputTo(AdmRequest request) throws BazisException {
        SitexReport report = this.esrn.report("globalPersonSearchProtocol");
        int group = 1;
        for (final Iterable<Person> persons : this.lists) {
            for (final Person person : persons)
                report = RtfProtocol.append(report, group, person);
            group++;
        }
        final File file = report.toFile(
            new ReportParams(
                this.esrn, request, new JoinedIterable<>(this.lists)
            )
        );
        request.set("protocol", this.esrn.downloadUrl(file));
    }

    private static SitexReport append(SitexReport report,
        Number group, Person person) throws BazisException {
        final Map<String, Object> row = new RtfPerson(person);
        SitexReport result = report;
        if (new IsEmpty(person.appoints()).value())
            result = result.append(group, row);
        else for (final Appoint appoint : person.appoints())
            result = result.append(
                group,
                new MapOf<>(new Entries<>(row, new RtfAppoint(appoint)))
            );
        return result;
    }

}
