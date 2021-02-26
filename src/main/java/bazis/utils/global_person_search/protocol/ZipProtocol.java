package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.map.Entries;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.FormattedText;
import bazis.cactoos3.text.JoinedText;
import bazis.sitex3.SitexReport;
import bazis.sitex3.misc.ReportRow;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.action.ResultAction;
import bazis.utils.global_person_search.dates.FormattedDate;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.ext.All;
import bazis.utils.global_person_search.ext.ReportData;
import bazis.utils.global_person_search.ext.Sum;
import bazis.utils.global_person_search.misc.ParamsOf;
import bazis.utils.global_person_search.misc.RequestPerson;
import bazis.utils.global_person_search.printed.PrintedPayouts;
import bazis.utils.global_person_search.printed.PrintedPeriods;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import sx.admin.AdmRequest;

public final class ZipProtocol implements Protocol {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final Esrn esrn;

    private final Iterable<Iterable<Person>> lists;

    public ZipProtocol(Esrn esrn) {
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
        final boolean isEmpty = new CheckedScalar<>(
            new All<>(
                this.lists,
                new Func<Iterable<Person>, Boolean>() {
                    @Override
                    public Boolean apply(Iterable<Person> list) {
                        return new IsEmpty(list).value();
                    }
                }
            )
        ).value();
        final Opt<Number> personId = new ParamsOf(request).personId();
        final Person person = personId.has()
            ? this.esrn.person(personId.get())
            : new RequestPerson(request);
        final ReportData params = new ReportData.Immutable()
            .withString(
                "currentDate",
                new FormattedDate("dd.MM.yyyy HH:mm:ss", new Date())
            )
            .withDate("startDate", new ParamsOf(request).startDate())
            .withDate("endDate", new ParamsOf(request).endDate())
            .withString(
                "mspList",
                new JoinedText(
                    ", ", this.esrn.measures(
                    new ParamsOf(request).msp()
                ).values()
                )
            )
            .withString("failures", (String) request.get("fails"))
            .withString("message", isEmpty ? ResultAction.NO_RESULT : "")
            .withInt(
                "num",
                this.esrn.iteratorValue("globalPersonSearchIterator")
            )
            .withString("fromDate", new HumanDate(new Date()))
            .withString("org", this.esrn.orgName())
            .withString("fio", person.fio())
            .withString("birthdate", new HumanDate(person.birthdate()))
            .withString("snils", person.snils())
            .withString("docName", "Паспорт гражданина России")
            .withString("docNumber", person.passport())
            .withString(
                "requestPeriod",
                new FormattedText(
                    "%s - %s",
                    new HumanDate(
                        new ParamsOf(request).startDate()
                    ).asString(),
                    new HumanDate(
                        new ParamsOf(request).endDate()
                    ).asString()
                )
            )
            .withString(
                "resultMessage",
                new FormattedText(
                    "Данные в других СЗН Республики Мордовия %s",
                    isEmpty ? "не найдены" : "найдены"
                )
            );
        final Collection<File> files = new LinkedList<>();
        int counter = 1;
        for (final Iterable<Person> persons : this.lists)
            for (final Person pers : persons)
                files.add(
                    ZipProtocol.append(
                        this.esrn.report("globalPersonSearchProtocol"),
                        1, pers
                    ).toFile(
                        new MapOf<>(
                            new Entries<>(
                                params,
                                new ReportRow()
                                    .withInt("filename", counter++)
                            )
                        )
                    )
                );
        request.set(
            "protocol", this.esrn.downloadUrl(ZipProtocol.toZip(files))
        );
    }

    private static SitexReport append(SitexReport report,
        Number group, Person person) throws BazisException {
        final ReportData row = new ReportData.Immutable()
            .withInt("personId", ZipProtocol.COUNTER.getAndIncrement())
            .withString("borough", person.borough())
            .withString(
                "person",
                new JoinedText(
                    ", ",
                    new IterableOf<>(
                        person.fio(),
                        new HumanDate(person.birthdate()).asString(),
                        person.address()
                    )
                )
            )
            .withString(
                "passport", new FormattedText(
                    "%s, %s", person.snils(), person.passport()
                )
            )
            .withString(
                "sum",
                new FormattedText(
                    "%.02f",
                    new Sum(
                        new MappedIterable<>(
                            new JoinedIterable<>(
                                new MappedIterable<>(
                                    person.appoints(),
                                    new Func<Appoint, Iterable<Payout>>() {
                                        @Override
                                        public Iterable<Payout> apply(
                                            Appoint appoint) {
                                            return appoint.payouts();
                                        }
                                    }
                                )
                            ),
                            new Func<Payout, Number>() {
                                @Override
                                public Number apply(Payout payout) {
                                    return payout.sum();
                                }
                            }
                        )
                    ).doubleValue()
                )
            );
        SitexReport result = report;
        for (final Appoint appoint : person.appoints())
            result = result.append(
                11,
                new ReportRow()
                    .withString("msp", appoint.msp())
                    .withString("category", appoint.category())
                    .withString("child", appoint.child())
                    .withString(
                        "period", new PrintedPeriods(appoint.periods())
                    )
                    .withString("status", appoint.status())
                    .withString(
                        "payments", new PrintedPayouts(appoint.payouts())
                    )
            );
        return result.append(group, row);
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
