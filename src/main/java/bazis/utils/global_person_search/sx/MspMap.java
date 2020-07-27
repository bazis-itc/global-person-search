package bazis.utils.global_person_search.sx;

import bazis.cactoos3.Func;
import bazis.cactoos3.Scalar;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.map.MapEnvelope;
import bazis.cactoos3.map.MapOf;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.utils.global_person_search.ext.Entries;
import java.util.Map;
import sx.common.SXUtils;
import sx.datastore.SXObj;
import sx.datastore.SXObjList;
import sx.datastore.meta.SXClass;
import sx.datastore.params.SXObjListParams;

final class MspMap extends MapEnvelope<String, String> {

    private static final String GUID_ATTR = "guid", NAME_ATTR = "name";

    MspMap(final String identifiers) {
        super(
            new CachedScalar<>(
                new Scalar<Map<String, String>>() {
                    @Override
                    public Map<String, String> value() throws Exception {
                        final SXObjList list =
                            new SXObjListParams("pprServ")
                                .setDefaultAttrCollection(SXClass.NONE_ATTRS)
                                .addSelectedAttr(MspMap.GUID_ATTR)
                                .addSelectedAttr(MspMap.NAME_ATTR)
                                .addIdInList(SXUtils.getIdList(identifiers))
                                .getObjList();
                        return new MapOf<>(
                            new Entries<>(
                                list == null
                                    ? new EmptyIterable<SXObj>()
                                    : (Iterable<SXObj>) list,
                                new Func<SXObj, String>() {
                                    @Override
                                    public String apply(SXObj msp) {
                                        return msp.getStringAttr(
                                            MspMap.GUID_ATTR
                                        );
                                    }
                                },
                                new Func<SXObj, String>() {
                                    @Override
                                    public String apply(SXObj msp) {
                                        return msp.getStringAttr(
                                            MspMap.NAME_ATTR
                                        );
                                    }
                                }
                            )
                        );
                    }
                }
            )
        );
    }

}
