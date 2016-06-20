package adventure.persistence;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.List;

public class PostgreSQLFullTextSearchFunction implements SQLFunction {

    @Override
    public Type getReturnType(Type columnType, Mapping mapping)
            throws QueryException {
        return new BooleanType();
    }

    @Override
    public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) throws QueryException {
//        if (args.size() != 3) {
//            throw new IllegalArgumentException(
//                    "The function must be passed 3 arguments");
//        }

//        String ftsConfig = (String) args.get(0);
//        String field = (String) args.get(1);
//        String value = (String) args.get(2);

        String field = (String) args.get(0);
        String value = (String) args.get(1);

        String fragment = "to_tsvector(portuguese::regconfig, unaccent(" + field + ")) @@ "
                + "to_tsquery(portuguese, unaccent(" + value + "))";


        return fragment;
    }

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return false;
    }
}