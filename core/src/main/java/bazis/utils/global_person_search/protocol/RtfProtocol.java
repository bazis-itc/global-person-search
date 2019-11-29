package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.FormattedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.Report;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.ext.Lines;
import bazis.utils.global_person_search.ext.ReportData;
import bazis.utils.global_person_search.sx.DownloadUrl;
import java.util.Date;
import java.util.Map;
import sx.admin.AdmRequest;

public final class RtfProtocol implements Protocol {

    private final Report report;

    private final Number group;

    public RtfProtocol(Report report) {
        this(report, 1);
    }

    private RtfProtocol(Report report, Number group) {
        this.report = report;
        this.group = group;
    }

    @Override
    public Protocol append(Iterable<Person> persons) throws BazisException {
        int counter = 1;
        for (final Person person : persons)
            //noinspection ValueOfIncrementOrDecrementUsed
            this.write(counter++, person);
        return new RtfProtocol(this.report, this.group.intValue() + 1);
    }

    @Override
    public void outputTo(AdmRequest request,
        Map<String, Object> params) throws BazisException {
        request.set(
            "protocol",
            new DownloadUrl(this.report.create(params)).asString()
        );
    }

    private void write(Number id, Person person) throws BazisException {
        final ReportData row = new ReportData.Immutable()
            .withInt("personId", id)
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
                "passport",
                new FormattedText(
                    "%s, %s", person.snils(), person.passport()
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
                            this.dateAsString("с", appoint.startDate()),
                            this.dateAsString("по", appoint.endDate())
                        )
                    )
                    .withString("status", appoint.status())
                    .withString("payments", appoint.payments())
            );
    }

    @SuppressWarnings("MethodMayBeStatic")
    private String dateAsString(String prefix, Opt<Date> date)
        throws BazisException {
        return date.has()
            ? String.format(
                "%s %s", prefix, new HumanDate(date.get()).asString()
            )
            : "";
    }

}
