package bazis.utils.global_person_search;

import bazis.cactoos3.exception.BazisException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.xml.bind.DatatypeConverter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public final class Tests {

    @Test
    public void name() throws Exception {
        final String message = "exception message";
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        new ObjectOutputStream(output).writeObject(new BazisException(message));
        try (
            final ObjectInputStream input = new ObjectInputStream(
                new ByteArrayInputStream(
                    DatatypeConverter.parseBase64Binary(
                        DatatypeConverter.printBase64Binary(
                            output.toByteArray()
                        )
                    )
                )
            )
        ) {
            MatcherAssert.assertThat(
                BazisException.class.cast(input.readObject()).getMessage(),
                Matchers.equalTo(message)
            );
        }
    }

}
