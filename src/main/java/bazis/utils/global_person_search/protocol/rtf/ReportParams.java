package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.Opt;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.cactoos3.scalar.IsEmpty;
import bazis.cactoos3.text.FormattedText;
import bazis.cactoos3.text.JoinedText;
import bazis.sitex3.misc.ReportRow;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.action.ResultAction;
import bazis.utils.global_person_search.dates.FormattedDate;
import bazis.utils.global_person_search.dates.HumanDate;
import bazis.utils.global_person_search.misc.ParamsOf;
import bazis.utils.global_person_search.misc.RequestPerson;
import java.util.Date;
import java.util.Map;
import sx.admin.AdmRequest;

final class ReportParams extends MapEnvelope<String, Object> {

    ReportParams(final Esrn esrn,
        final AdmRequest request, final Iterable<?> results) {
        super(
            new CachedScalar<>(
                new Scalar<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> value() throws BazisException {
                        return ReportParams.map(esrn, request, results);
                    }
                }
            )
        );
    }

    private static Map<String, Object> map(Esrn esrn, AdmRequest request,
        Iterable<?> results) throws BazisException {
        final boolean isEmpty = new IsEmpty(results).value();
        final Opt<Number> personId =
            new ParamsOf(request).personId();
        final Person person = personId.has()
            ? esrn.person(personId.get())
            : new RequestPerson(request);
        final Date start = new ParamsOf(request).startDate();
        final Date end = new ParamsOf(request).endDate();
        return new ReportRow()
            .withString(
                "currentDate",
                new FormattedDate("dd.MM.yyyy HH:mm:ss", new Date())
            )
            .withString("startDate", new HumanDate(start))
            .withString("endDate", new HumanDate(end))
            .withString(
                "mspList",
                new JoinedText(
                    ", ",
                    esrn.measures(new ParamsOf(request).msp()).values()
                )
            )
            .withString("failures", (String) request.get("fails"))
            .withString("message", isEmpty ? ResultAction.NO_RESULT : "")
            .withInt(
                "num",
                esrn.iteratorValue("globalPersonSearchIterator")
            )
            .withString("fromDate", new HumanDate(new Date()))
            .withString("org", esrn.orgName())
            .withString("fio", person.fio())
            .withString("birthdate", new HumanDate(person.birthdate()))
            .withString("snils", person.snils())
            .withString("docName", "Паспорт гражданина России")
            .withString("docNumber", person.passport())
            .withString(
                "requestPeriod",
                new FormattedText(
                    "%s - %s",
                    new HumanDate(start).asString(),
                    new HumanDate(end).asString()
                )
            )
            .withString(
                "resultMessage",
                new FormattedText(
                    "Данные в других СЗН Республики Мордовия %s",
                    isEmpty ? "не найдены" : "найдены"
                )
            );
    }

}
