package bazis.utils.global_person_search.ext;

import bazis.cactoos3.Func;
import bazis.cactoos3.collection.ListOf;
import bazis.cactoos3.exception.BazisException;
import bazis.cactoos3.iterable.JoinedIterable;
import bazis.cactoos3.iterable.MappedIterable;
import bazis.utils.global_person_search.Report;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public final class HtmlReport implements Report {

    private final File file;

    private final Map<Integer, Group> groups;

    public HtmlReport(File file) {
        this.file = file;
        this.groups = new TreeMap<>();
    }

    @Override
    public Report append(Number group, Map<String, Object> row) {
        if (!this.groups.containsKey(group.intValue()))
            this.groups.put(
                group.intValue(), new Group(new LinkedList<Record>())
            );
        final Group grp = this.groups.get(group.intValue());
        grp.records.add(new Record(row));
        return this;
    }

    @Override
    public File create(Map<String, Object> params) throws BazisException {
        this.groups.put(0, new Group(new ListOf<>(new Record(params))));
        try (
            final PrintWriter writer =
                new PrintWriter(new FileWriter(this.file))
        ) {
            writer.printf("<html>%n<body>%n");
            for (final Map.Entry<Integer, Group> entry : this.groups.entrySet()) {
                final Iterable<String> fields = new HashSet<>(
                    new ListOf<>(
                        new JoinedIterable<>(
                            new MappedIterable<>(
                                entry.getValue().records,
                                new Func<Record, Iterable<String>>() {
                                    @Override
                                    public Iterable<String> apply(Record record) {
                                        return record.fields.keySet();
                                    }
                                }
                            )
                        )
                    )
                );
                writer.printf("<h3>#Query.%d#</h3>%n", entry.getKey());
                writer.println("<table border=\"1\">");
                writer.println("\t<tr>");
                for (final String field : fields)
                    writer.printf("\t\t<th>%s</th>%n", field);
                writer.println("\t</tr>");
                for (final Record record : entry.getValue().records) {
                    writer.println("\t<tr>");
                    for (final String field : fields)
                        writer.printf(
                            "\t\t<td><pre>%s</pre></td>%n",
                            record.fields.containsKey(field)
                                ? record.fields.get(field) : ""
                        );
                    writer.println("\t</tr>");
                }
                writer.println("</table>");
            }
            writer.printf("</body>%n</html>");
        } catch (final IOException ex) {
            throw new BazisException(ex);
        }
        return this.file;
    }

}

final class Group {

    final Collection<Record> records;

    Group(Collection<Record> records) {
        this.records = records;
    }

}

final class Record {

    final Map<String, Object> fields;

    Record(Map<String, Object> fields) {
        this.fields = fields;
    }

}
