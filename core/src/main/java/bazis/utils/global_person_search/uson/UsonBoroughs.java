package bazis.utils.global_person_search.uson;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.ext.Entries;
import bazis.utils.global_person_search.ext.Lines;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import org.jooq.Record;
import org.jooq.impl.DSL;

public final class UsonBoroughs extends MapEnvelope<Integer, Borough> {

    public UsonBoroughs(
        final Connection connection, final Collection<String> log) {
        this(
            new Scalar<Map<Integer, Borough>>() {
                @Override
                public Map<Integer, Borough> value() throws Exception {
                    return new MapOf<>(
                        new Entries<>(
                            DSL.using(connection).fetch(
                                new Lines(
                                    "",
                                    "SELECT",
                                    "  A_OUID AS id,",
                                    "  A_IP_ADRESS_RAION AS url,",
                                    "  A_RAION_NAME AS name",
                                    "FROM REFERENCE_INF"
                                ).asString()
                            ),
                            new Func<Record, Integer>() {
                                @Override
                                public Integer apply(Record record) {
                                    return record.getValue(
                                        "id", Integer.class
                                    );
                                }
                            },
                            new Func<Record, Borough>() {
                                @Override
                                public Borough apply(Record record) {
                                    return new UsonBorough(record, log);
                                }
                            }
                        )
                    );
                }
            }
        );
    }

    private UsonBoroughs(Scalar<Map<Integer, Borough>> scalar) {
        super(new CachedScalar<>(scalar));
    }

}
