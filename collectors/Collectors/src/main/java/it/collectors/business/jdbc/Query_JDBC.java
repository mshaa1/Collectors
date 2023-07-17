package it.collectors.business.jdbc;

import com.mysql.cj.PreparedQuery;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import it.collectors.controller.RicercaController;
import it.collectors.model.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;
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
    //TODO c'è un gap vuoto, non ci sta la funzionalita 14

    // Funzionalità 35
    // get numero duplicati dischi
    public Map<Disco,Integer> getNumeroDuplicatiDischi(int IDCollezionista){
        Map<Disco,Integer> duplicati = new HashMap<>();
        try {
            CallableStatement statement = connection.prepareCall("{call get_numero_duplicati_dischi(?)}");
            statement.setInt(1, IDCollezionista);
            statement.execute();
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                duplicati.put(
                        new Disco(
                                result.getInt("ID"),
                                result.getString("titolo"),
                                result.getInt("anno_uscita"),
                                result.getString("barcode"),
                                result.getString("formato"),
                                result.getString("stato_conservazione"),
                                result.getString("descrizione_conservazione")
                        ),result.getInt("numero_duplicati")
                );
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return duplicati;
    }


    // Funzionalità 34
    // rimozione di un collezione dal database
    public void removeCollezione(int IDCollezione){
        try {
            CallableStatement statement = connection.prepareCall("{call rimuovi_collezione(?)}");
            statement.setInt(1, IDCollezione);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Funzionalità 33
    // rimozione di una traccia nel database
    public void removeTraccia(int IDTraccia){
        try {
            CallableStatement statement = connection.prepareCall("{call rimuovi_traccia(?)}");
            statement.setInt(1, IDTraccia);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Funzionalità 32
    // rimozione di un disco nel database
    public void removeDisco(int IDDisco){
        try {
            CallableStatement statement = connection.prepareCall("{call rimuovi_disco(?)}");
            statement.setInt(1, IDDisco);
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // Funzionalità 31
    // Get tutte le collezioni condivise con singolo utente e il nome del proprietario

    public Map<Collezione,Collezionista> getCollezioniCondiviseProprietario(int IDCollezionista){
        Map<Collezione,Collezionista> collezioni = new HashMap<>();

        try{
            CallableStatement statement = connection.prepareCall("{call get_collezioni_condivise_e_proprietario(?)}");
             statement.setInt(1, IDCollezionista);
             statement.execute();

            ResultSet result = statement.getResultSet();
            while (result.next()){
                Collezione collezione = new Collezione(
                        result.getInt(1),
                        result.getString(2),
                        result.getBoolean(3)
                );
                Collezionista proprietario = new Collezionista(
                        result.getInt(4),
                        result.getString(5),
                        result.getString(6)
                );
                collezioni.put(collezione,proprietario);
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return collezioni;
    }

    // Funzionalità 29
    // Get Autori di un disco

    public List<Autore> getAutoriDisco(int IDDisco){
        CallableStatement statement = null;
        List<Autore> autori = new ArrayList<>();
        try {
            statement = connection.prepareCall("{call get_autori_disco(?)}");
            statement.setInt(1, IDDisco);

            statement.execute();
            ResultSet result = statement.getResultSet();

            while (result.next()) {
                Autore autore = new Autore(
                        result.getInt("ID"),
                        result.getString("nome"),
                        result.getString("cognome"),
                        result.getDate("data_nascita"),
                        result.getString("nome_autore"),
                        result.getString("info"),
                        result.getString("ruolo")
                );
                autori.add(autore);
            }
            result.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autori;
    }

    // Funzionalità 28
    // get tutti i dischi di un utente

    public List<Disco> getDischiUtente(int IDUtente){

        List<Disco> dischi = new ArrayList<>();
        try{
            CallableStatement statement = connection.prepareCall("{call get_dischi_utente(?)}");
            statement.setInt(1,IDUtente);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()){
                Disco disco = new Disco(
                        resultSet.getInt("ID"),
                        resultSet.getString("titolo"),
                        resultSet.getInt("anno_uscita"),
                        resultSet.getString("barcode"),
                        resultSet.getString("formato"),
                        resultSet.getString("stato_conservazione"),
                        resultSet.getString("descrizione_conservazione")
                );

                dischi.add(disco);
            }
            resultSet.close();
            statement.close();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return dischi;
    }

// Funzionalità 27
    // get genere di un disco

    public Genere getGenere(int IDDisco) {
        Genere genere = null;
        try {
            CallableStatement statement = connection.prepareCall("{call get_genere_disco(?)}");
            statement.setInt(1, IDDisco);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                genere = new Genere(
                        resultSet.getInt("ID"),
                        resultSet.getString("nome")
                );
            }
            resultSet.close();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return genere;
    }

    // Funzionalità 26
    // get autori
    public List<Autore> getAutori(){
        List<Autore> autori = new ArrayList<>();
        try {
            CallableStatement statement = connection.prepareCall("{call get_autori()}");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Autore autore = new Autore(
                        resultSet.getInt("ID"),
                        resultSet.getString("nome"),
                        resultSet.getString("cognome"),
                        resultSet.getDate("data_nascita"),
                        resultSet.getString("nome_autore"),
                        resultSet.getString("info"),
                        resultSet.getString("ruolo")
                );

                autori.add(autore);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return autori;
    }

    // Funzionalità 25
    // numero di collezoni totali per il singolo utente loggato
    public int numeroCollezioniCollezionista(int IDCollezionista) {
        try {
            CallableStatement statement = connection.prepareCall("{call statistiche_numero_collezioni_collezionista(?)}");
            statement.setInt(1, IDCollezionista);
            statement.execute();
            ResultSet set = statement.getResultSet();
            int num = -2;
            if (set.next()) num = set.getInt(1);
            statement.close();
            return num;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;

    }




    // Funzionalità 24
    // get etichetta di un disco

    public Etichetta getEtichetta(int IDDisco) {
        Etichetta etichetta = null;
        try {
            CallableStatement statement = connection.prepareCall("{call get_etichetta_disco(?)}");
            statement.setInt(1, IDDisco);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                etichetta = new Etichetta(
                        resultSet.getInt("ID"),
                        resultSet.getString("nome"),
                        resultSet.getString("sede_legale"),
                        resultSet.getString("email")
                );
            }
            resultSet.close();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return etichetta;
    }


    // Funzionalità 23
    // get tutti i dichi di un utente con etichetta

    public Map<Disco,Etichetta> getDischiUtenteEtichetta(int IDUtente) {

        //ID, titolo, anno_uscita, barcode, formato, stato_conservazione, descrizione_conservazione, etichetta_id, nome, sede_legale, email

        Map<Disco,Etichetta> dischiEtichetta = new HashMap<>();
        try {
            CallableStatement statement = connection.prepareCall("{call get_dischi_utente_etichetta(?)}");
            statement.setInt(1, IDUtente);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                dischiEtichetta.put(
                        new Disco(
                                resultSet.getInt("ID"),
                                resultSet.getString("titolo"),
                                resultSet.getInt("anno_uscita"),
                                resultSet.getString("barcode"),
                                resultSet.getString("formato"),
                                resultSet.getString("stato_conservazione"),
                                resultSet.getString("descrizione_conservazione")
                        ), new Etichetta(
                                resultSet.getInt("etichetta_id"),
                                resultSet.getString("nome"),
                                resultSet.getString("sede_legale"),
                                resultSet.getString("email")
                                )
                );
            }
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dischiEtichetta;
    }




    // Funzionalità 22
    // ottieni collezioni utente
    public List<Collezione> getCollezioniUtente(int IDUtente) {
        List<Collezione> collezioni = new ArrayList<>();
        try {
            CallableStatement statement = connection.prepareCall("{call get_collezioni_utente(?)}");
            statement.setInt(1, IDUtente);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Collezione collezione = new Collezione(
                        resultSet.getInt("ID"), // ID collezione
                        resultSet.getString("nome"), // nome collezione
                        resultSet.getBoolean("flag") // flag collezione
                );
                collezioni.add(collezione);
            }
            resultSet.close();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return collezioni;
    }

    // Funzionalità 21
    // ottieni ID utente
    public Integer getIDUtente(String nickname, String email) {
        Integer ID = null;
        try {
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
    // Funzionalità 20
    //registrazione utente
    public boolean registrazioneUtente(String nickname, String email) {
        try {
            CallableStatement statement = connection.prepareCall("{call registrazione_utente(?,?)}");
            statement.setString(1, nickname);
            statement.setString(2, email);

            statement.execute();


            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
        return true;
    }

    // Funzionalità 19
    // convalida accesso utente
    public Boolean getAccesso(String nickname, String email) {
        Boolean risultato = false;
        try {
            CallableStatement statement = connection.prepareCall("{call convalida_utente(?,?,?)}");
            statement.setString(1, nickname);
            statement.setString(2, email);
            statement.registerOutParameter(3, Types.BOOLEAN);
            statement.execute();

            risultato = statement.getBoolean(3);
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return risultato;
    }


    // Funzionalità 16
    // aggiunta autore
    public void aggiuntaAutore(String nome, String cognome, Date dataNascita, String nomeAutore, String info, String ruolo) {
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
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // Funzionalità 17
    // aggiunta genere
    public boolean aggiuntaGenere(String genere) {
        try {
            CallableStatement statement = connection.prepareCall("{call aggiunta_genere(?)}");
            statement.setString(1, genere);
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException sqlException) {
            if(sqlException.getSQLState().equals("45001")) return false;
            else sqlException.printStackTrace();
        }
        return false;
    }

    // Funzionalità 18
    // rimozione genere
    public void rimozioneGenere(int id) {
        try {
            CallableStatement statement = connection.prepareCall("{call rimozione_genere(?)}");
            statement.setInt(1, id);
            statement.execute();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }



    // Funzionalità 2

    // aggiunta di dischi a una collezione

    public void inserimentoDiscoInCollezione(int IDDisco, int IDCollezione) {
        try {
            CallableStatement statement = connection.prepareCall("{call inserisci_disco_collezione(?,?)}");
            statement.setInt(1, IDDisco);
            statement.setInt(2, IDCollezione);

            statement.execute();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // aggiunta di tracce ad un disco
    public void inserimentoTracceInDisco(int IDDisco, String titolo, int durataOre, int durataMinuti, int durataSecondi) {
        try {
            CallableStatement statement = connection.prepareCall("{call inserisci_tracce_disco(?,?,?)}");
            statement.setInt(1, IDDisco);
            statement.setString(2, titolo);
            statement.setTime(3, new java.sql.Time(durataOre, durataMinuti, durataSecondi));

            statement.execute();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    // Funzionalità 3
    // Modifica dello stato di pubblicazione di una collezione

    public void modificaFlagCollezione(int IDCollezione, boolean flag) {

        try {
            CallableStatement statement = connection.prepareCall("{call modifica_flag_collezione(?,?)}");
            statement.setInt(1, IDCollezione);
            statement.setBoolean(2, flag);
            statement.execute();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // Funzionalità 15
    // Aggiunta di nuove condivisioni a una collezione

    public void inserisciCondivisione(int IDCollezione, int IDCollezionista) {
        try {
            CallableStatement statement = connection.prepareCall("{call inserisci_condivisione(?,?)}");
            statement.setInt(1, IDCollezione);
            statement.setInt(2, IDCollezionista);
            statement.execute();
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    // Funzionalità 4
    // Rimozione di un disco da una collezione

    public void rimozioneDiscoCollezione(int IDCollezione, int IDDisco) {
        try {
            CallableStatement statement = connection.prepareCall("{call rimozione_disco_collezione(?,?)}");

            statement.setInt(1, IDDisco);
            statement.setInt(2, IDCollezione);
            statement.execute();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // Funzionalità 5
    // Rimozione di una collezione

    public void rimozioneCollezione(int IDCollezione) {
        try {
            CallableStatement statement = connection.prepareCall("{call rimozione_collezione(?)}");
            statement.setInt(1, IDCollezione);

            statement.execute();
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    // Funzionalità 6
    // Ottengo tutti i dischi di una collezione
    public ArrayList<Disco> listaDischiCollezione(int IDCollezione) {
        ArrayList<Disco> listaDischi = new ArrayList<Disco>();
        try {
            CallableStatement statement = connection.prepareCall("{call lista_dischi_collezione(?)}");
            statement.setInt(1, IDCollezione);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next())
                listaDischi.add(
                        new Disco(
                                resultSet.getInt(1), //ID
                                resultSet.getString(2), // titolo
                                resultSet.getInt(3), //anno di uscita
                                resultSet.getString(4), //barcode
                                resultSet.getString(5), //formato
                                resultSet.getString(6), // stato di conservazione
                                resultSet.getString(7) // descrizione conservazione
                        )
                );
            statement.close();
            if (listaDischi.isEmpty()) return null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return listaDischi;
    }

    // Funzionalità 7
    // Tracklist di un disco
    public ArrayList<Traccia> tracklistDisco(int IDDisco) {
        ArrayList<Traccia> tracklist = new ArrayList<>();
        try {
            CallableStatement statement = connection.prepareCall("{call tracklist_disco(?)}");
            statement.setInt(1, IDDisco);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next())
                tracklist.add(
                        new Traccia(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getTime(3)
                        )
                );
            statement.close();
            if (tracklist.isEmpty()) return null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return tracklist;
    }

    // Funzionalità 8
    // Ricerca di dischi in base al nome autore e/o titolo del disco

    public ArrayList<Disco> getRicercaDischiPerAutoreTitolo(String nomeAutore, String titoloDisco, Boolean pubbliche , Boolean condivise, Boolean private_, int IDCollezionista) {
        ArrayList<Disco> dischi = new ArrayList<>();
        try {
            CallableStatement statement = connection.prepareCall("{call ricerca_dischi_per_autore_titolo(?,?,?,?,?,?)}");
            if(nomeAutore==null) statement.setNull(1, Types.VARCHAR);
            statement.setString(1, nomeAutore);

            if(titoloDisco == null) statement.setNull(2, Types.VARCHAR);
            else statement.setString(2, titoloDisco);

            statement.setBoolean(3, pubbliche);
            statement.setBoolean(4, condivise);
            statement.setBoolean(5, private_);

            statement.setInt(6, IDCollezionista);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {

                Disco disco = new Disco(
                        resultSet.getInt(1), //ID
                        resultSet.getString(2), // titolo
                        resultSet.getInt(3), //anno di uscita
                        resultSet.getString(4), //barcode
                        resultSet.getString(5), //formato
                        resultSet.getString(6), // stato di conservazione
                        resultSet.getString(7) // descrizione conservazione
                );
                //System.out.println(disco.getTitolo());
                dischi.add(disco);
            }
            statement.close();
            if (dischi.isEmpty()) return null;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        //System.out.println(dischi.size());
        return dischi;
    }

    // Funzionalità 9
    // Verifica della visibilità di una collezione da parte di un collezionista

    public boolean getVerificaVisibilitaCollezione(int IDCollezione, int IDCollezionista) {
        boolean risultato = false;
        try {
            PreparedStatement statement = connection.prepareStatement("select verifica_visibilita_collezione(?,?)");
            statement.setInt(1, IDCollezione);
            statement.setInt(2, IDCollezionista);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                risultato = rs.getBoolean(1);
            }
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return risultato;

        // ritorna falso anche se la tabella restituita non esiste
    }

    // Funzionalità 10
    // numero di tracce di dischi distinti di un certo autore presenti nelle collezioni pubbliche


    public Integer getNumeroTracceDistintePerAutoreCollezioniPubblice(int IDAutore) {
        Integer risultato = null;
        try {
            CallableStatement statement = connection.prepareCall("{call numero_tracce_distinte_per_autore_collezioni_pubbliche(?,?)}");
            statement.setInt(1, IDAutore);
            statement.registerOutParameter(2, Types.INTEGER);
            statement.execute();
            risultato = statement.getInt(2);
            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return risultato;
    }

    // Funzionalità 11
    // minuti totali di musica riferibili a un certo autore memorizzati nelle collezioni pubbliche
    public int getMinutiTotaliMusicaPerAutore(int IDAutore) {
        int minuti = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("select minuti_totali_musica_pubblica_per_autore(?)");
            statement.setInt(1, IDAutore);
            statement.execute();

            ResultSet resultset = statement.getResultSet();
            if (resultset.next()) {
                minuti = resultset.getInt(1);
            }

            statement.close();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return minuti;
    }

    // Funzionalità 12
    // statistiche: numero collezioni di ciascun collezionista
    public Map<Collezionista, Integer> getStatisticheNumeroCollezioni() {
        try {
            HashMap<Collezionista, Integer> stats = new HashMap<>();
            CallableStatement statement = connection.prepareCall("{call statistiche_numero_collezioni()}");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                stats.put(new Collezionista(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                ), resultSet.getInt(4));
            }
            statement.close();
            return stats;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }


    // statistiche: numero di dischi per genere nel sistema
    public Map<Genere, Integer> getStatisticheDischiPerGenere() {
        HashMap<Genere, Integer> genereNumeroDischi = new HashMap<>();
        try {
            CallableStatement statement = connection.prepareCall("{call statistiche_dischi_per_genere()}");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                genereNumeroDischi.put(
                        new Genere(
                            resultSet.getInt(1),
                            resultSet.getString(2)
                            ),
                        resultSet.getInt(3)
                );
            }
            statement.close();
            return genereNumeroDischi;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    // Funzionalità 13
    public HashMap<Disco, Integer> dischiSimiliA(String barcode, String titolo, String autore) {
        HashMap<Disco, Integer> dischi = new HashMap<>();
        try {
            PreparedStatement queryBarcode = connection.prepareStatement("select * from disco where barcode like ?");
            PreparedStatement queryTitolo = connection.prepareStatement("select * from disco where titolo like ?");
            PreparedStatement queryAutore = connection.prepareStatement(
                    "select d.* from " +
                            "disco d join produce_disco p on d.ID=p.ID_disco" +
                            "join autore a on p.ID_autore=a.ID" +
                            "where nome_autore like ?"
            );

            queryBarcode.setString(1, barcode);
            queryTitolo.setString(1, titolo);
            queryAutore.setString(1, autore);


            ResultSet barcodeResult = queryBarcode.executeQuery();
            ResultSet titoloResult = queryTitolo.executeQuery();
            ResultSet autoreResult = queryAutore.executeQuery();

            while (barcodeResult.next()) {
                dischi.put(
                        new Disco(
                                barcodeResult.getInt(1), //ID
                                barcodeResult.getString(2), // titolo
                                barcodeResult.getInt(3), //anno di uscita
                                barcodeResult.getString(4), //barcode
                                barcodeResult.getString(5), //formato
                                barcodeResult.getString(6), // stato di conservazione
                                barcodeResult.getString(7) // descrizione conservazione
                        ), 1
                );
            }

            while (titoloResult.next()) {
                Disco disco = new Disco(

                        titoloResult.getInt(1), //ID
                        titoloResult.getString(2), // titolo
                        titoloResult.getInt(3), //anno di uscita
                        titoloResult.getString(4), //barcode
                        titoloResult.getString(5), //formato
                        titoloResult.getString(6), // stato di conservazione
                        titoloResult.getString(7) // descrizione conservazione
                );

                if (dischi.containsKey(disco))
                    dischi.put(disco, dischi.get(disco) + 1);
                else
                    dischi.put(disco, 1);
            }

            while (autoreResult.next()) {
                Disco disco = new Disco(
                        autoreResult.getInt(1), //ID
                        autoreResult.getString(2), // titolo
                        autoreResult.getInt(3), //anno di uscita
                        autoreResult.getString(4), //barcode
                        autoreResult.getString(5), //formato
                        autoreResult.getString(6), // stato di conservazione
                        autoreResult.getString(7) // descrizione conservazione
                );
                if (dischi.containsKey(disco))
                    dischi.put(disco, dischi.get(disco) + 1);
                else
                    dischi.put(disco, 1);
            }

            queryBarcode.close();
            queryTitolo.close();
            queryAutore.close();
            if (dischi.isEmpty()) return null;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dischi;
    }

    // inserimento collezione
    // Funzionalità 1
    public int inserimentoCollezione(String nome, boolean flag, int IDCollezionista) {
        try {
            CallableStatement statement = connection.prepareCall("{call inserisci_collezione(?,?,?,?)}");
            statement.setString(1, nome);
            statement.setBoolean(2, flag);
            statement.setInt(3, IDCollezionista);
            statement.registerOutParameter(4, Types.INTEGER);
            statement.execute();
            int id = statement.getInt(4);
            statement.close();
            return id;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return -1;
    }

        // inserimento di una nuova etichetta
        // Funzionalità 32
        public boolean inserimentoEtichetta(String nome, String sede_legale, String email) {
            try {
                CallableStatement statement = connection.prepareCall("{call aggiunta_etichetta(?,?,?)}");
                statement.setString(1, nome);
                statement.setString(2, sede_legale);
                statement.setString(3, email);

                statement.execute();
                statement.close();
                return true;
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }

    // inserimento di una nuovo disco
    // Funzionalità 33

        public boolean inserisciDisco(Integer idUtente, String titolo, Integer annoUscita, String barcode, String formato, String statoConservazione, String descrizioneConservazione, Integer idEtichetta, Integer idGenere) {
            try {
                CallableStatement statement = connection.prepareCall("{call inserisci_disco(?,?,?,?,?,?,?,?,?)}");
                statement.setInt(1, idUtente);
                statement.setString(2, titolo);
                statement.setInt(3, annoUscita);
                statement.setString(4, barcode);
                statement.setString(5, formato);
                statement.setString(6, statoConservazione);
                statement.setString(7, descrizioneConservazione);
                statement.setInt(8, idEtichetta);
                statement.setInt(9, idGenere);

                statement.execute();
                statement.close();
                return true;
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return false;
        }


    // ottenimento generi del sistema
    // Funzionalità 34

    public Set<Genere> get_Generi_Sistema() {
        Set<Genere> generi = new HashSet<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from genere");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                generi.add(new Genere(resultSet.getInt(1), resultSet.getString(2)));
            }
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return generi;
    }

    // ottenimento etichette del sistema
    // Funzionalità 35

    public Set<Etichetta> get_Etichette_Sistema() {
        Set<Etichetta> etichette = new HashSet<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from etichetta");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                etichette.add(new Etichetta(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return etichette;
    }



 }
