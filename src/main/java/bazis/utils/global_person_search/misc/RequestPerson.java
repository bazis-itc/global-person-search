package bazis.utils.global_person_search.misc;

import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Appoint;
import bazis.utils.global_person_search.Person;
import java.util.Date;
import sx.admin.AdmRequest;

public final class RequestPerson implements Person {

    private final AdmRequest request;

    public RequestPerson(AdmRequest request) {
        this.request = request;
    }

    @Override
    public String fio() {
        return String.format(
            "%s %s %s",
            this.string("surname"),
            this.string("name"),
            this.string("patronymic")
        ).trim();
    }

    @Override
    public Date birthdate() throws BazisException {
        final String value = this.string("birthdate");
        if (value.isEmpty())
            throw new BazisException("Person birthdate not defined");
        return new Date(Long.parseLong(value));
    }

    @Override
    public String address() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public String snils() {
        return this.string("snils");
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
    public Iterable<Appoint> appoints() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    private String string(String param) {
        final String value = this.request.getParam(param);
        return value == null ? "" : value;
    }

}
