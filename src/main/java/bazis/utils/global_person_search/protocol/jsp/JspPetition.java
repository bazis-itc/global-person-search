package bazis.utils.global_person_search.protocol.jsp;

import bazis.cactoos3.Opt;
import bazis.cactoos3.exception.BazisException;
import bazis.utils.global_person_search.Petition;
import bazis.utils.global_person_search.dates.HumanDate;
import java.util.Date;

public final class JspPetition {

    private final Petition petition;

    JspPetition(Petition petition) {
        this.petition = petition;
    }

    public String getMsp() {
        return this.petition.msp();
    }

    public String getCategory() {
        return this.petition.category();
    }

    public String getRegDate() throws BazisException {
        return new HumanDate(this.petition.regDate()).asString();
    }

    public String getAppointDate() throws BazisException {
        final Opt<Date> date = this.petition.appointDate();
        return date.has() ? new HumanDate(date.get()).asString() : "";
    }

    public String getStatus() {
        return this.petition.status();
    }

}
