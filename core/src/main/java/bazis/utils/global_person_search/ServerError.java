package bazis.utils.global_person_search;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.Text;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.scalar.ScalarOf;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import javax.xml.bind.DatatypeConverter;

@SuppressWarnings("LambdaUnfriendlyMethodOverload")
public final class ServerError implements Scalar<Exception>, Text {

    private final Scalar<Exception> scalar;

    public ServerError(Text serialized) {
        this(new ServerError.Parsed(serialized));
    }

    public ServerError(Exception exception) {
        this(new ScalarOf<>(exception));
    }

    private ServerError(Scalar<Exception> scalar) {
        this.scalar = scalar;
    }

    @Override
    public Exception value() throws BazisException {
        return new CheckedScalar<>(this.scalar).value();
    }

    @Override
    public String asString() throws BazisException {
        try (
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final ObjectOutput output = new ObjectOutputStream(bytes)
        ) {
            output.writeObject(this.value());
            return DatatypeConverter.printBase64Binary(bytes.toByteArray());
        } catch (final IOException ex) {
            throw new BazisException(ex);
        }
    }

    private static final class Parsed implements Scalar<Exception> {

        private final Text serialized;

        private Parsed(Text serialized) {
            this.serialized = serialized;
        }

        @Override
        public Exception value() throws Exception {
            try (
                final ObjectInput input = new ObjectInputStream(
                    new ByteArrayInputStream(
                        DatatypeConverter.parseBase64Binary(
                            this.serialized.asString()
                        )
                    )
                )
            ) {
                return Exception.class.cast(input.readObject());
            }
        }

    }

}
