package bazis.utils.global_person_search.sx;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.map.EmptyMap;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.sitex3.SitexReport;
import bazis.utils.global_person_search.Esrn;
import bazis.utils.global_person_search.Person;
import java.io.File;
import java.util.Map;
import sx.cms.CmsActionUtils;
import sx.cms.CmsApplication;
import sx.datastore.SXDsFactory;
import sx.datastore.SXId;
import sx.datastore.impl.SXIterator;
import sx.datastore.impl.fs.SXDsFs;
import sx.datastore.meta.SXClass;
import sx.datastore.params.SXObjListParams;

public final class SxEsrn implements Esrn {

    @Override
    public Person person(final Number id) throws BazisException {
        return new CheckedScalar<>(
            new Scalar<Person>() {
                @Override
                public Person value() throws Exception {
                    return new SxPerson(
                        new SXId("wmPersonalCard", id.intValue())
                    );
                }
            }
        ).value();
    }

    @Override
    public Map<String, String> measures(String links) {
        return links.isEmpty()
            ? new EmptyMap<String, String>() : new MspMap(links);
    }

    @Override
    public SitexReport report(String code) {
        return new SxReport(code);
    }

    @Override
    public String downloadUrl(File file) {
        return CmsActionUtils.getDownloadURL(
            SXDsFs.class.cast(SXDsFactory.getDs("reports"))
                .file2Obj(file).getId()
        );
    }

    @Override
    public Number iteratorValue(final String iterator) throws BazisException {
        return new CheckedScalar<>(
            new Scalar<Number>() {
                @Override
                public Number value() throws Exception {
                    return SXIterator.getSXIterator().next(iterator, true);
                }
            }
        ).value();
    }

    @Override
    public String orgName() throws BazisException {
        return new CheckedScalar<>(
            new Scalar<String>() {
                @Override
                public String value() throws Exception {
                    return new SXObjListParams(
                        (SXId) CmsApplication.getCmsApplication()
                            .getObject("regOrgName")
                    )
                        .setDefaultAttrCollection(SXClass.TITLE_ATTRS)
                        .getObj().getTitle();
                }
            }
        ).value();
    }

}
