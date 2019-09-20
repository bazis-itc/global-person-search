package bazis.utils.global_person_search.sx;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.cactoos3.scalar.UncheckedScalar;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import java.util.Date;
import sx.datastore.SXId;
import sx.datastore.SXObj;
import sx.datastore.meta.SXClass;
import sx.datastore.params.SXObjListParams;

public final class SxPerson implements Person {

    private static final String
        SURNAME = "Surname", NAME = "Name", PATRONYMIC = "SecondName",
        BIRTHDATE = "BirthDate", SNILS = "snils";

    private final UncheckedScalar<SXObj> person;

    public SxPerson(final SXId id) {
        this(
            new Scalar<SXObj>() {
                @Override
                public SXObj value() throws Exception {
                    return new SXObjListParams(id)
                        .setDefaultAttrCollection(SXClass.NONE_ATTRS)
                        .addSelectedAttr(
                            SxPerson.SURNAME,
                            SxPerson.NAME,
                            SxPerson.PATRONYMIC,
                            SxPerson.BIRTHDATE,
                            SxPerson.SNILS
                        )
                        .addSelectedAttr(SxPerson.SURNAME, SXClass.TITLE_ATTRS)
                        .addSelectedAttr(SxPerson.NAME, SXClass.TITLE_ATTRS)
                        .addSelectedAttr(
                            SxPerson.PATRONYMIC, SXClass.TITLE_ATTRS
                        )
                        .getObj();
                }
            }
        );
    }

    private SxPerson(Scalar<SXObj> scalar) {
        this.person = new UncheckedScalar<>(new CachedScalar<>(scalar));
    }

    @Override
    public String fio() {
        final String patronymic =
            this.person.value().getTitle(SxPerson.PATRONYMIC);
        return new UncheckedText(
            new JoinedText(
                " ",
                new IterableOf<>(
                    this.person.value().getTitle(SxPerson.SURNAME),
                    this.person.value().getTitle(SxPerson.NAME),
                    patronymic == null ? "" : patronymic
                )
            )
        ).asString();
    }

    @Override
    public Date birthdate() {
        return this.person.value().getDateAttr(SxPerson.BIRTHDATE);
    }

    @Override
    public String snils() {
        final String value = this.person.value().getStringAttr(SxPerson.SNILS);
        return value == null ? "" : value;
    }

    @Override
    public String address() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String borough() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String passport() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        throw new BazisException("Method not implemented");
    }

}
