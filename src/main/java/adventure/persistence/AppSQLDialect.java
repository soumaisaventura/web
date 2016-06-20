package adventure.persistence;

import org.hibernate.dialect.PostgreSQLDialect;

public class AppSQLDialect extends PostgreSQLDialect {

    public AppSQLDialect() {
        registerFunction("fts", new PostgreSQLFullTextSearchFunction());
    }
}
