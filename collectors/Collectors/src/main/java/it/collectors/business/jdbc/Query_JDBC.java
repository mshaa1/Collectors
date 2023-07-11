package it.collectors.business.jdbc;

import it.collectors.model.Collezione;
import it.collectors.model.Disco;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    // ottieni collezioni utente
    public List<Collezione> getCollezioniUtente(int IDUtente){
        List<Collezione> collezioni = new ArrayList<>();
        try{
            CallableStatement statement = connection.prepareCall("{call prendi_collezioni_utente(?)}");
            statement.setInt(1,IDUtente);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Collezione collezione = new Collezione(
                        resultSet.getInt("ID"),
                        resultSet.getString("nome"),
                        resultSet.getBoolean("flag")
                );
                collezioni.add(collezione);
            }
            resultSet.close();
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return collezioni;
    }

    // ottieni ID utente
    public Integer getIDUtente(String nickname, String email){
        Integer ID = null;
        try{
            CallableStatement statement = connection.prepareCall("{call prendi_ID_utente(?,?,?)}");
            statement.setString(1, nickname);
            statement.setString(2, email);
            statement.registerOutParameter(3, Types.INTEGER);
            statement.execute();

            ID = statement.getInt(3);
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return ID;
    }

    // convalida accesso utente
    public Boolean getAccesso(String nickname, String email){
        Boolean risultato = false;
        try {
            CallableStatement statement = connection.prepareCall("{call convalida_utente(?,?,?)}");
            statement.setString(1,nickname);
            statement.setString(2,email);
            statement.registerOutParameter(3, Types.BOOLEAN);
            statement.execute();

            risultato = statement.getBoolean(3);
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return risultato;
    }

    //registrazione utente
    public boolean registrazioneUtente(String nickname, String email){
        try{
            CallableStatement statement = connection.prepareCall("{call registrazione_utente(?,?)}");
            statement.setString(1,nickname);
            statement.setString(2,email);

            statement.execute();


            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
        return true;
    }

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
            statement.close();
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
            statement.close();

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
             statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    // inserimento collezione
    // Query 1
    public void inserimentoCollezione(String nome, boolean flag, int IDCollezionista){
        try{
            CallableStatement statement = connection.prepareCall("{call inserisci_collezione(?,?,?)}");
            statement.setString(1,nome);
            statement.setBoolean(2,flag);
            statement.setInt(3,IDCollezionista);

            statement.execute();
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    // aggiunta di dischi a una collezione
    // Query 2
    public void inserimentoDiscoInCollezione(int IDDisco, int IDCollezione){
        try{
            CallableStatement statement = connection.prepareCall("{call inserisci_disco_collezione(?,?)}");
            statement.setInt(1,IDDisco);
            statement.setInt(2,IDCollezione);

            statement.execute();
            statement.close();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    // aggiunta di tracce ad un disco
    // Query 3
    public void inserimentoTracceInDisco(int IDDisco, String titolo, int durataOre, int durataMinuti, int durataSecondi) {
        try {
            CallableStatement statement = connection.prepareCall("{call inserisci_tracce_disco(?,?,?)}");
            statement.setInt(1, IDDisco);
            statement.setString(2, titolo);
            statement.setTime(3, new java.sql.Time(durataOre, durataMinuti, durataSecondi));

            statement.execute();
            statement.close();
        }
        catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }



    // Modifica dello stato di pubblicazione di una collezione
    // Query 4
    public void modificaFlagCollezione(int IDCollezione, boolean flag){

        try{
            CallableStatement statement = connection.prepareCall("{call modifica_flag_collezione(?,?)}");
            statement.setInt(1,IDCollezione);
            statement.setBoolean(2,flag);
            statement.execute();
            statement.close();

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    // Aggiunta di nuove condivisioni a una collezione
    //Query 5

    public void inserisciCondivisione(int IDCollezione, int IDCollezionista){
        try{
            CallableStatement statement = connection.prepareCall("{call inserisci_condivisione(?,?)}");
            statement.setInt(1,IDCollezione);
            statement.setInt(2,IDCollezionista);
            statement.execute();
            statement.close();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }



    //Rimozione di un disco da una collezione
    // Query 6
    public void rimozioneDiscoCollezione(int IDCollezione, int IDDisco){
        try{
            CallableStatement statement = connection.prepareCall("{call rimozione_disco_collezione(?,?)}");

            statement.setInt(1, IDDisco);
            statement.setInt(2, IDCollezione);
            statement.execute();
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    //Rimozione di una collezione
    //Query 7
    public void rimozioneCollezione(int IDCollezione) {
        try{
            CallableStatement statement = connection.prepareCall("{call rimozione_collezione(?)}");
            statement.setInt(1,IDCollezione);

            statement.execute();
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    //Ricerca di dischi in base al nome autore e/o titolo del disco
    //Query 8
    public ArrayList<Disco> getRicercaDischiPerAutoreTitolo(String nomeAutore, String titoloDisco, boolean flag, int IDCollezionista) {
        ArrayList<Disco> dischi = new ArrayList<>();
        try{
            CallableStatement statement = connection.prepareCall("{call ricerca_dischi_per_autore_titolo(?,?,?,?)}");
            statement.setString(1, nomeAutore);
            statement.setString(2, titoloDisco);
            statement.setBoolean(3, flag);
            statement.setInt(4, IDCollezionista);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                Disco disco = new Disco(
                        resultSet.getInt(1), //ID
                        resultSet.getString(2), // titolo
                        resultSet.getInt(3), //anno di uscita
                        resultSet.getString(4), //barcode
                        resultSet.getString(5), //formato
                        resultSet.getString(6), // stato di conservazione
                        resultSet.getString(7) // descrizione conservazione
                );
                dischi.add(disco);
            }

            statement.close();
            return dischi;
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }




    // Verifica della visibilità di una collezione da parte di un collezionista
    //Query 9
    public boolean getVerificaVisibilitaCollezione(int IDCollezione, int IDCollezionista){
        boolean risultato = false;
        try{
            PreparedStatement statement = connection.prepareStatement("select verifica_visibilita_collezione(?,?)");
            statement.setInt(1,IDCollezione);
            statement.setInt(2,IDCollezionista);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if(rs.next()){
                 risultato = rs.getBoolean(1);
            }
            statement.close();
         }catch (SQLException sqlException){
        sqlException.printStackTrace();
        }
        return risultato;

        // ritorna falso anche se la tabella restituita non esiste
    }

    // numero di tracce di dischi distinti di un certo autore presenti nelle collezioni pubbliche
    // Query 10

    public Integer getNumeroTracceDistintePerAutoreCollezioniPubblice(int IDAutore){
        Integer risultato = null;
        try{
            CallableStatement statement = connection.prepareCall("{call numero_tracce_distinte_per_autore_collezioni_pubbliche(?,?)}");
            statement.setInt(1,IDAutore);
            statement.registerOutParameter(2, Types.INTEGER);
            statement.execute();
            risultato =  statement.getInt(2);
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return risultato;
    }

    //minuti totali di musica riferibili a un certo autore memorizzati nelle collezioni pubbliche
    //Query 11
    public int getMinutiTotaliMusicaPerAutore(int IDAutore){
        int minuti = 0;
        try{
            PreparedStatement statement = connection.prepareStatement("select minuti_totali_musica_pubblica_per_autore(?)");
            statement.setInt(1, IDAutore);
            statement.execute();

            ResultSet resultset = statement.getResultSet();
            if (resultset.next())
            {
                minuti = resultset.getInt(1);
            }

            statement.close();

        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return minuti;
    }

    // Query 12a
    // statistiche: numero collezioni di ciascun collezionista
    public int getStatisticheNumeroCollezioni(){
        int stats = 0;
        try {
            CallableStatement statement = connection.prepareCall("{call statistiche_numero_collezioni(?)}");
            statement.registerOutParameter(1,Types.INTEGER);
            statement.execute();
            stats = statement.getInt(1);
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return stats;
    }


}

