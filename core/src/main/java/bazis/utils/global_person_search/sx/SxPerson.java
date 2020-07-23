package bazis.utils.global_person_search.sx;

import bazis.cactoos3.Scalar;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.scalar.CachedScalar;
import bazis.cactoos3.scalar.CheckedScalar;
import bazis.cactoos3.text.JoinedText;
import bazis.cactoos3.text.UncheckedText;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import java.util.Date;
import sx.datastore.SXId;
import sx.datastore.SXObj;
import sx.datastore.meta.SXClass;
import sx.datastore.params.SXObjListParams;

final class SxPerson implements Person {

    private static final String
        SURNAME = "Surname", NAME = "Name", PATRONYMIC = "SecondName",
        BIRTHDATE = "BirthDate", SNILS = "snils";

    private final CheckedScalar<SXObj> person;

    SxPerson(final SXId id) {
        this(
            new Scalar<SXObj>() {
                @Override
                public SXObj value() throws Exception {
                    final String titleAttr = "name";
                    return new SXObjListParams(id)
                        .setDefaultAttrCollection(SXClass.NONE_ATTRS)
                        .addSelectedAttr(
                            SxPerson.SURNAME,
                            SxPerson.NAME,
                            SxPerson.PATRONYMIC,
                            SxPerson.BIRTHDATE,
                            SxPerson.SNILS
                        )
                        .addSelectedAttr(SxPerson.SURNAME, titleAttr)
                        .addSelectedAttr(SxPerson.NAME, titleAttr)
                        .addSelectedAttr(SxPerson.PATRONYMIC, titleAttr)
                        .getObj();
                }
            }
        );
    }

    private SxPerson(Scalar<SXObj> scalar) {
        this.person = new CheckedScalar<>(new CachedScalar<>(scalar));
    }

    @Override
    public String fio() throws BazisException {
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
    public Date birthdate() throws BazisException {
        final Date date = this.person.value().getDateAttr(SxPerson.BIRTHDATE);
        if (date == null)
            throw new BazisException("Person birthdate not defined");
        return date;
    }

    @Override
    public String snils() throws BazisException {
        final String value = this.person.value().getStringAttr(SxPerson.SNILS);
        return value == null ? "" : value;
    }

    @Override
    public String address() throws BazisException {
        throw new BazisException("Method not implemented");
    }

    @Override
    public String borough() throws BazisException {
        throw new BazisException("Method not implemented");
    }

    @Override
    public String passport() throws BazisException {
        throw new BazisException("Method not implemented");
    }

    @Override
    public Iterable<Appoint> appoints() throws BazisException {
        throw new BazisException("Method not implemented");
    }

}
