package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Func;
import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.FormattedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.Report;
import bazis.utils.global_person_search.action.ResultAction;
import bazis.utils.global_person_search.dates.FormattedDate;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.dates.Period;
import bazis.utils.global_person_search.ext.All;
import bazis.utils.global_person_search.ext.ReportData;
import bazis.utils.global_person_search.ext.Sum;
import bazis.utils.global_person_search.misc.ParamsOf;
import bazis.utils.global_person_search.misc.PrintedPayouts;
import bazis.utils.global_person_search.misc.RequestPerson;
import java.io.File;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import sx.admin.AdmRequest;

public final class RtfProtocol implements Protocol {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

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
        Report report = this.esrn.report("globalPersonSearchProtocol");
        int group = 1;
        for (final Iterable<Person> persons : this.lists) {
            for (final Person person : persons)
                report = RtfProtocol.append(report, group, person);
            group++;
        }
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
        final File file = report.create(
            new ReportData.Immutable()
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
                )
        );
        request.set("protocol", this.esrn.downloadUrl(file));
        "".toCharArray();
    }

    private static Report append(Report report, Number group, Person person)
        throws BazisException {
        final ReportData row = new ReportData.Immutable()
            .withInt("personId", RtfProtocol.COUNTER.getAndIncrement())
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
        Report result = report;
        if (new IsEmpty(person.appoints()).value())
            result = result.append(group, row);
        else for (final Appoint appoint : person.appoints())
            //noinspection HardcodedLineSeparator
            result = result.append(
                group,
                row
                    .withString("msp", appoint.msp())
                    .withString("category", appoint.category())
                    .withString("child", appoint.child())
                    .withString(
                        "period",
                        new Period(
                            "\n", appoint.startDate(), appoint.endDate()
                        )
                    )
                    .withString("status", appoint.status())
                    .withString(
                        "payments", new PrintedPayouts(appoint.payouts())
                    )
            );
        return result;
    }

}
