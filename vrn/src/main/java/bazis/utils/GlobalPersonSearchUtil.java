package bazis.utils;

import bazis.utils.global_person_search.BaseSearchUtil;

@SuppressWarnings({
    "WeakerAccess", "ClassIndependentOfModule", "ClassOnlyUsedInOneModule"
})
public final class GlobalPersonSearchUtil extends BaseSearchUtil {

    public GlobalPersonSearchUtil() {
        super("http://192.168.155.9:8080/central/");
    }

}
