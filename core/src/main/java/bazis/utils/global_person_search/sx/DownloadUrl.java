package bazis.utils.global_person_search.sx;

import bazis.cactoos3.Text;
import java.io.File;
import sx.cms.CmsActionUtils;
import sx.datastore.SXDsFactory;
import sx.datastore.impl.fs.SXDsFs;

public final class DownloadUrl implements Text {

    private final File file;

    public DownloadUrl(File file) {
        this.file = file;
    }

    @Override
    public String asString() {
        return CmsActionUtils.getDownloadURL(
            SXDsFs.class.cast(SXDsFactory.getDs("reports"))
                .file2Obj(this.file).getId()
        );
    }

}
