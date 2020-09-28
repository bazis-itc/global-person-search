package bazis.utils.global_person_search.protocol;

import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.iterable.EmptyIterable;
import bazis.cactoos3.iterable.IterableOf;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.utils.global_person_search.Person;
import bazis.utils.global_person_search.Protocol;
import bazis.utils.global_person_search.misc.Config;
import bazis.utils.global_person_search.protocol.jsp.JspPersonList;
import sx.admin.AdmRequest;

public final class JspProtocol implements Protocol {

    private static final String DISPLAY_PAYMENTS = "displayPayments";

    private final String title;

    private final Iterable<JspPersonList> lists;

    public JspProtocol() {
        this("", new EmptyIterable<JspPersonList>());
    }

    private JspProtocol(String title, Iterable<JspPersonList> lists) {
        this.title = title;
        this.lists = lists;
    }

    @Override
    public Protocol append(Iterable<Person> persons) {
        return new JspProtocol(
            this.title.isEmpty() ? "Найдено совпадение только по СНИЛС" : "",
            new JoinedIterable<>(
                this.lists,
                new IterableOf<>(
                    new JspPersonList(this.title, persons)
                )
            )
        );
    }

    @Override
    public void outputTo(AdmRequest request) {
        request.set("lists", new ListOf<>(this.lists));
        request.set(
            JspProtocol.DISPLAY_PAYMENTS,
            Boolean.parseBoolean(new Config().get(JspProtocol.DISPLAY_PAYMENTS))
        );
    }

}
