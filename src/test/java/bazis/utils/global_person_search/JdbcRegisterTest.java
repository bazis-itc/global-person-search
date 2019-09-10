package bazis.utils.global_person_search;

import bazis.utils.global_person_search.jdbc.JdbcRegister;
import bazis.utils.global_person_search.json.JsonPersons;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.DriverManager;

public final class JdbcRegisterTest {

    public static void main(String... args) throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        try (
            final Connection connection = DriverManager.getConnection(
                "jdbc:sqlserver://10.65.12.5;" +
                    "databaseName=update_test_central;" +
                    "user=sa;password=S1tex2016"
            );
        ) {
            System.out.println(
                new GsonBuilder().setPrettyPrinting().create().toJson(
                    new JsonPersons(
                        new JdbcRegister(connection)
                            .persons("14590614872")
                    ).asJson()
                )
            );
        }
    }

}