package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.*;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class DischiController implements Initializable, DataInitializable<Collezionista>{

    @FXML
    private TableView<DiscoWrapper> table;

    @FXML
    private TableColumn<DiscoWrapper, String> titolo;

    @FXML
    private TableColumn<DiscoWrapper, String> annoUscita;

    @FXML
    private TableColumn<DiscoWrapper, String> barcode;

    @FXML
    private TableColumn<DiscoWrapper, String> formato;

    @FXML
    private TableColumn<DiscoWrapper, String> statoConservazione;

    @FXML
    private TableColumn<DiscoWrapper, String> descrizione;

    @FXML
    private TableColumn<DiscoWrapper, String> etichetta;

    @FXML
    private TableColumn<DiscoWrapper, String> genere;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    Collezionista collezionista;

    protected class DiscoWrapper {
        private Disco disco;
        private Etichetta etichetta;
        private Genere genere;

        public DiscoWrapper(Disco disco, Etichetta etichetta, Genere genere) {
            this.disco = disco;
            this.etichetta = etichetta;
            this.genere = genere;
        }

        public Disco getDisco() {
            return disco;
        }

        public String getEtichetta() {
            return etichetta.getNome();
        }

        public String getGenere() {
            return genere.getNome();
        }

        public String getTitolo() {
            return disco.getTitolo();
        }

        public Integer getAnnoUscita() {
            return disco.getAnnoUscita();
        }

        public String getBarcode() {
            return disco.getBarcode();
        }

        public String getFormato() {
            return disco.getFormato();
        }

        public String getStatoConservazione() {
            return disco.getStatoConservazione();
        }

        public String getDescrizioneConservazione() {
            return disco.getDescrizioneConservazione();
        }

        public Integer getIdDisco() {
            return disco.getId();
        }

        public Integer getIdEtichetta() {
            return etichetta.getId();
        }

        public Integer getIdGenere() {
            return genere.getId();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;

        Map<Disco,Etichetta> dischiEtichetta = queryJdbc.getDischiUtenteEtichetta(data.getId());
        List<DiscoWrapper> dischi = new ArrayList<>();

        for(Map.Entry<Disco,Etichetta> entry: dischiEtichetta.entrySet()){
            dischi.add(new DiscoWrapper(
                            entry.getKey(),
                            entry.getValue(),
                            queryJdbc.getGenere(entry.getKey().getId())
                    )
            );
        }

        titolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        annoUscita.setCellValueFactory(new PropertyValueFactory<>("annoUscita"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        statoConservazione.setCellValueFactory(new PropertyValueFactory<>("statoConservazione"));
        descrizione.setCellValueFactory(new PropertyValueFactory<>("descrizioneConservazione"));
        etichetta.setCellValueFactory(new PropertyValueFactory<>("etichetta"));
        genere.setCellValueFactory(new PropertyValueFactory<>("genere"));
        for(DiscoWrapper d: dischi){
            table.getItems().add(d);
        }
    }

    @FXML
    public void add() {

    }

    @FXML
    public void remove() {

    }

    @FXML
    public void ricerca() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.navigateTo(Pages.RICERCA, collezionista);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void goToHome() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.navigateTo(Pages.HOME,collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
