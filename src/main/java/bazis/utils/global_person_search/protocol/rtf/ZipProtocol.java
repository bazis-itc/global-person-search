package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.map.Entries;
import bazis.cactoos3.map.MapOf;
import bazis.sitex3.SitexReport;
import bazis.sitex3.misc.ReportRow;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.ext.TextOf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import sx.admin.AdmRequest;

final class ZipProtocol implements Protocol {

    private final Esrn esrn;

    private final Iterable<Iterable<Person>> lists;

    ZipProtocol(Esrn esrn) {
        this(esrn, new EmptyIterable<Iterable<Person>>());
    }

    private ZipProtocol(Esrn esrn, Iterable<Iterable<Person>> lists) {
        this.esrn = esrn;
        this.lists = lists;
    }

    @Override
    public Protocol append(Iterable<Person> persons) {
        return new ZipProtocol(
            this.esrn, new JoinedIterable<>(
                this.lists, new IterableOf<>(persons)
            )
        );
    }

    @Override
    public void outputTo(AdmRequest request) throws BazisException {
        final Map<String, Object> params = new ReportParams(
            this.esrn, request, new JoinedIterable<>(this.lists)
        );
        final Collection<File> files = new LinkedList<>();
        int counter = 1, group = 1;
        for (final Iterable<Person> persons : this.lists) {
            for (final Person person : persons) {
                SitexReport report = this.esrn
                    .report("globalPersonSearchProtocol")
                    .append(group, new RtfPerson(person));
                for (final Petition petition : person.petitions())
                    report = report.append(
                        group * 10 + 1,
                        new ReportRow()
                            .withString("msp", petition.msp())
                            .withString("category", petition.category())
                            .withString(
                                "regDate", new HumanDate(petition.regDate())
                            )
                            .withString(
                                "appointDate",
                                petition.appointDate().has()
                                    ? new HumanDate(
                                    petition.appointDate().get()
                                )
                                    : new TextOf("")
                            )
                            .withString("status", petition.status())
                    );
                for (final Appoint appoint : person.appoints())
                    report = report.append(
                        group * 10 + 2, new RtfAppoint(appoint)
                    );
                files.add(
                    report.toFile(
                        new MapOf<>(
                            new Entries<>(
                                params,
                                new ReportRow()
                                    .withInt("filename", counter++)
                            )
                        )
                    )
                );
            }
            group++;
        }
        request.set(
            "protocol", this.esrn.downloadUrl(ZipProtocol.toZip(files))
        );
    }

    private static File toZip(Iterable<File> files) throws BazisException {
        final File result = new File(
            new ListOf<>(files).get(0).getParentFile(),
            "report.zip"
        );
        try (
            final FileOutputStream output = new FileOutputStream(result);
            final ZipOutputStream zip = new ZipOutputStream(output)
        ) {
            for (final File file : files)
                try (final InputStream input = new FileInputStream(file)) {
                    final ZipEntry entry = new ZipEntry(file.getName());
                    zip.putNextEntry(entry);
                    final byte[] bytes = new byte[1024];
                    int length;
                    while ((length = input.read(bytes)) >= 0)
                        zip.write(bytes, 0, length);
                }
        } catch (final IOException ex) {
            throw new BazisException(ex);
        }
        return result;
    }

}
