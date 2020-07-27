package bazis.utils.global_person_search.fake;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Report;
import java.io.File;
import java.util.Collections;
import java.util.Map;

public final class FakeEsrn implements Esrn {

    private final Report report;

    public FakeEsrn() {
        this(
            new Report() {
                @Override
                public Report append(Number group, Map<String, Object> row) {
                    return this;
                }
                @Override
                public File create(Map<String, Object> params)
                    throws BazisException {
                    throw new BazisException("Report not defined");
                }
            }
        );
    }

    public FakeEsrn(Report report) {
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
    public Report report(String code) {
        return this.report;
    }

    @Override
    public String downloadUrl(File file) {
        return file.getAbsolutePath();
    }

}
