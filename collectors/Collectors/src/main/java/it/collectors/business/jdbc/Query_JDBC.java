package it.collectors.business.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Query_JDBC {

    private Connection connection;
    private boolean supports_procedures;
    private boolean supports_function_calls;

    public Query_JDBC(Connection c) throws ApplicationException {
        connect(c);
    }

    public final void connect(Connection c) throws ApplicationException {
        disconnect();
        this.connection = c;
        //verifichiamo quali comandi supporta il DBMS corrente
        supports_procedures = false;
        supports_function_calls = false;
        try {
            supports_procedures = connection.getMetaData().supportsStoredProcedures();
            supports_function_calls = supports_procedures && connection.getMetaData().supportsStoredFunctionsUsingCallSyntax();
        } catch (SQLException ex) {
            Logger.getLogger(Query_JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void disconnect() throws ApplicationException {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                System.out.println("\n**** CHIUSURA CONNESSIONE (modulo query) ************");
                this.connection.close();
                this.connection = null;
            }
        } catch (SQLException ex) {
            throw new ApplicationException("Errore di disconnessione", ex);
        }
    }


    //********************QUERY***************************//


    // aggiunta autore
    public void aggiuntaAutore(String nome, String cognome, Date dataNascita, String nomeAutore, String info, String ruolo){
        try {
            CallableStatement statement = connection.prepareCall("{call aggiunta_autore(?,?,?,?,?,?)}");
            statement.setString(1, nome);
            statement.setString(2, cognome);
            statement.setDate(3, new java.sql.Date(dataNascita.getTime()));
            statement.setString(4, nomeAutore);
            statement.setString(5, info);
            statement.setString(6, ruolo);

            statement.execute();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    // aggiunta genere
    public void aggiuntaGenere(String genere){
        try {
            CallableStatement statement = connection.prepareCall("{call aggiunta_genere(?)}");
            statement.setString(1, genere.toLowerCase());

            statement.execute();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    // rimozione genere
    public void rimozioneGenere(int id){
        try{
            CallableStatement statement = connection.prepareCall("{call rimozione_genere(?)}");
            statement.setInt(1,id);
             statement.execute();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    // inserimento collezione
    // Query 1
    public void inserimentoCollezione(String nome, boolean flag, int ID_collezionista){
        try{
            CallableStatement statement = connection.prepareCall("{call inserisci_collezione(?,?,?)}");
            statement.setString(1,nome);
            statement.setBoolean(2,flag);
            statement.setInt(3,ID_collezionista);

            statement.execute();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    // aggiunta di dischi a una collezione
    // Query 2
    public void inserimentoDiscoInCollezione(int ID_disco, int ID_collezione){
        try{
            CallableStatement statement = connection.prepareCall("{call inserisci_disco_collezione(?,?)}");
            statement.setInt(1,ID_disco);
            statement.setInt(2,ID_collezione);

            statement.execute();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}

