package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.Report;
import bazis.utils.global_person_search.ext.Lines;
import bazis.utils.global_person_search.ext.ReportData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public final class RtfProtocol implements Protocol {

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("dd.MM.yyyy");

    private final Report report;

    private final Number group;

    private final AtomicInteger counter;

    public RtfProtocol(Report report, Number group) {
        this.report = report;
        this.group = group;
        this.counter = new AtomicInteger(0);
    }

    @Override
    public void write(Person person) throws BazisException {
        final ReportData row = new ReportData.Immutable()
            .withInt("personId", this.counter.incrementAndGet())
            .withString("borough", person.borough())
            .withString(
                "person",
                new JoinedText(
                    ", ",
                    new IterableOf<>(
                        person.fio(),
                        RtfProtocol.DATE_FORMAT.format(
                            person.birthdate()
                        ),
                        person.address()
                    )
                )
            );
        if (new IsEmpty(person.appoints()).value())
            this.report.append(this.group, row);
        else for (final Appoint appoint : person.appoints())
            this.report.append(
                this.group,
                row
                    .withString("msp", appoint.msp())
                    .withString("category", appoint.category())
                    .withString("child", appoint.child())
                    .withString(
                        "period",
                        new Lines(
                            this.dateAsString("с ", appoint.startDate()),
                            this.dateAsString("по ", appoint.endDate())
                        )
                    )
                    .withString("status", appoint.status())
            );
    }

    private String dateAsString(String prefix, Opt<Date> date) {
        return date.has()
            ? prefix + RtfProtocol.DATE_FORMAT.format(date.get()) : "";
    }

}
