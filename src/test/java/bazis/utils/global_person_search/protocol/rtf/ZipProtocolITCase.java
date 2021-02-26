package bazis.utils.global_person_search.protocol.rtf;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.sitex3.SitexReport;
import bazis.utils.global_person_search.Payout;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.fake.FakeAppoint;
import bazis.utils.global_person_search.fake.FakeEsrn;
import bazis.utils.global_person_search.fake.FakePerson;
import bazis.utils.global_person_search.fake.FakeRequest;
import java.io.File;
import org.junit.Ignore;
import org.junit.Test;
import sx.common.reportsystem.MockReport;

@Ignore
public final class ZipProtocolITCase {

    @Test
    public void itCase() throws BazisException {
        final Iterable<Person> persons = new IterableOf<Person>(
            new FakePerson(),
            new FakePerson(
                "Пустой Иван Иванович",
                new FakeAppoint().withPayouts(new EmptyIterable<Payout>())
            )
        );
        new ZipProtocol(
            new FakeEsrn(
                new Scalar<SitexReport>() {
                    private int counter = 0;
                    @Override
                    public SitexReport value() {
                        this.counter++;
                        return new MockReport(
                            new File(
                                MockReport.class
                                    .getResource("/uln.rtf").getFile()
                            ),
                            new File(
                                "output",
                                String.format("%d.rtf", this.counter)
                            )
                        );
                    }
                }
            )
        ).append(persons).append(persons).outputTo(new FakeRequest());
    }

}