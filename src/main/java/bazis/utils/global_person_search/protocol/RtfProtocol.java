package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.text.CheckedText;
import bazis.cactoos3.text.JoinedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.Report;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class RtfProtocol implements Protocol {

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("dd.MM.yyyy");

    private final Report report;

    private final Number group;

    public RtfProtocol(Report report, Number group) {
        this.report = report;
        this.group = group;
    }

    @Override
    public void write(Person person) throws BazisException {
        final String people =
            new CheckedText(
                new JoinedText(
                    " ",
                    new IterableOf<>(
                        person.fio(),
                        RtfProtocol.DATE_FORMAT.format(
                            person.birthdate()
                        ),
                        person.address()
                    )
                )
            ).asString();
        for (final Appoint appoint : person.appoints()) {
            this.report.append(
                this.group,
                new Report.Data()
                    .withString("person", people)
                    .withString("msp", appoint.msp())
                    .withString("category", appoint.category())
                    .withString("child", appoint.child())
                    .withString(
                        "period",
                        String.format(
                            "%s\n%s",
                            this.dateAsString("с ", appoint.startDate()),
                            this.dateAsString("по ", appoint.startDate())
                        )
                    )
                    .withString("status", appoint.status())
            );
        }
    }

    private String dateAsString(String prefix, Opt<Date> date) {
        return date.has()
            ? prefix + RtfProtocol.DATE_FORMAT.format(date.get()) : "";
    }

}
