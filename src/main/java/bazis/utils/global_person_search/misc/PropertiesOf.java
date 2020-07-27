package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.MapEnvelope;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class PropertiesOf extends MapEnvelope<String, String> {

    public PropertiesOf(final Class<?> cls, final String path) {
        super(
            new Scalar<Map<String, String>>() {
                @Override
                public Map<String, String> value() throws Exception {
                    final InputStream resource = cls.getResourceAsStream(path);
                    if (resource == null)
                        throw new BazisException("Properties file not found");
                    final Properties properties = new Properties();
                    properties.load(resource);
                    final Map<String, String> map =
                        new HashMap<>(properties.size());
                    for (final String name: properties.stringPropertyNames())
                        map.put(name, properties.getProperty(name));
                    return Collections.unmodifiableMap(map);
                }
            }
        );
    }

}
