package bazis.utils;

import bazis.utils.global_person_search.BaseSearchUtil;

@SuppressWarnings({
    "ClassIndependentOfModule", "ClassOnlyUsedInOneModule"
})
public final class GlobalPersonSearchUtil extends BaseSearchUtil {

    public GlobalPersonSearchUtil() {
        super("http://10.3.0.54:9180/mord_central/");
    }

}
