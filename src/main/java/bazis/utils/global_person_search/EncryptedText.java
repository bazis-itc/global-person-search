package bazis.utils.global_person_search;

import bazis.cactoos3.Text;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import org.apache.commons.io.IOUtils;
import sx.bazis.uninfoobmen.sys.function.Function;
import sx.bazis.uninfoobmen.sys.store.DataObject;

public final class EncryptedText implements Text {

    private static final String ALGORITHM = "SHA-1", ENCODING = "CP1251";

    private final Text origin;

    public EncryptedText(final String str) {
        this(
            //todo import TextOf
            new Text() {
                @Override
                public String asString() {
                    return str;
                }
            }
        );
    }

    public EncryptedText(final InputStream stream) {
        this(
            new Text() {
                @Override
                public String asString() throws Exception {
                    return IOUtils.toString(
                        new DigestInputStream(
                            stream,
                            MessageDigest.getInstance(EncryptedText.ALGORITHM)
                        ),
                        EncryptedText.ENCODING
                    );
                }
            }
        );
    }

    public EncryptedText(Text origin) {
        this.origin = origin;
    }

    @Override
    public String asString() throws Exception {
        return this.origin.asString();
    }

    public DataObject asBytes() throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final MessageDigest crypto =
            MessageDigest.getInstance(EncryptedText.ALGORITHM);
        IOUtils.write(
            this.asString(),
            new DigestOutputStream(output, crypto),
            EncryptedText.ENCODING
        );
        final DataObject response = new DataObject(
            Function.byteArrayToHexString(crypto.digest())
        );
        response.setRetByte(output.toByteArray());
        return response;
    }

}
