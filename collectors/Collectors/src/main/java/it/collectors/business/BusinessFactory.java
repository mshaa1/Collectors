package it.collectors.business;

import it.collectors.business.jdbc.DatabaseImpl;
import it.collectors.business.jdbc.Query_JDBC;

public abstract class BusinessFactory {

    private static DatabaseImpl databaseImpl = new DatabaseImpl();

    public static Query_JDBC getImplementation() {
        return databaseImpl.getImplementation();
    }

    public static Query_JDBC reOpenConnection() {
        if (databaseImpl.getImplementation().getConnection() == null){
            databaseImpl = new DatabaseImpl();
        }
        return databaseImpl.getImplementation();
    }
}
