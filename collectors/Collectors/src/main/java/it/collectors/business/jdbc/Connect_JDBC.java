package it.collectors.business.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect_JDBC {

    private Connection connection;

    private final String username;

    private String password;

    private final String connection_string;


    public Connect_JDBC(String username, String password, String connection_string) {
        this.username = username;
        this.password = password;
        this.connection_string = connection_string;
        this.connection = null;
    }


    // Tale metodo ritorna una nuova connessione al database solo se
    // la connessione non è già stata aperta precedentemente.
    public Connection getConnection() throws ApplicationException{
        if (this.connection == null) {
            connection = connect();
        }
        return this.connection;
    }

    // Questo metodo restituisce una nuova connessione ad ogni chiamata
    public Connection newConnection() throws ApplicationException{
        return connect();
    }

    // Metodo che gestisce la connessione al database
    private Connection connect() throws ApplicationException{
        try{
            if(this.username != null && this.password != null){
                this.connection = DriverManager.getConnection(this.connection_string, this.username, this.password);
            } else {
                this.connection = DriverManager.getConnection(this.connection_string);
            }
            return this.connection;
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    // Metodo che gestisce la disconnessione dal database
    public void disconnect() throws ApplicationException{
        try{
            if(this.connection != null && !this.connection.isClosed()){
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

}
