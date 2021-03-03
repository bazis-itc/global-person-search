package bazis.utils.global_person_search.misc;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.utils.global_person_search.Borough;
import bazis.utils.global_person_search.ext.Entries;
import bazis.utils.global_person_search.ext.Lines;
import java.util.Collection;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Record;

public final class UsonBoroughs extends MapEnvelope<Integer, Borough> {

    public UsonBoroughs(
        final DSLContext context, final Collection<String> fails) {
        this(
            new Scalar<Map<Integer, Borough>>() {
                @Override
                public Map<Integer, Borough> value() throws Exception {
                    //noinspection SpellCheckingInspection
                    return new MapOf<>(
                        new Entries<>(
                            context.fetch(
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
                                    return new UsonBorough(record, fails);
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
