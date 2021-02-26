package bazis.utils.global_person_search.fake;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.ScalarOf;
import bazis.cactoos3.scalar.UncheckedScalar;
import bazis.sitex3.SitexReport;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import java.io.File;
import java.util.Collections;
import java.util.Map;

public final class FakeEsrn implements Esrn {

    private final Scalar<SitexReport> report;

    public FakeEsrn() {
        this(
            new SitexReport() {
                @Override
                public SitexReport append(Number group, Map<String, Object> row) {
                    return this;
                }
                @Override
                public File toFile(Map<String, Object> params)
                    throws BazisException {
                    throw new BazisException("Report not defined");
                }
            }
        );
    }

    public FakeEsrn(SitexReport report) {
        this(new ScalarOf<>(report));
    }

    public FakeEsrn(Scalar<SitexReport> report) {
        this.report = report;
    }

    @Override
    public Person person(Number id) {
        return new FakePerson();
    }

    @Override
    public Map<String, String> measures(String links) {
        return Collections.singletonMap(
            "37145780-704c-48a2-9272-1f99afddaa9f",
            "Ежемесячная денежная компенсация военнослужащим"
        );
    }

    @Override
    public SitexReport report(String code) {
        return new UncheckedScalar<>(this.report).value();
    }

    @Override
    public String downloadUrl(File file) {
        return file.getAbsolutePath();
    }

    @Override
    public Number iteratorValue(String iterator) {
        return 256;
    }

    @Override
    public String orgName() {
        return "Отдел по Пролетарскому району";
    }

}
