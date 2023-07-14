package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.*;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
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

    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

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
        titolo.setReorderable(false);
        annoUscita.setReorderable(false);
        barcode.setReorderable(false);
        formato.setReorderable(false);
        statoConservazione.setReorderable(false);
        descrizione.setReorderable(false);
        etichetta.setReorderable(false);
        genere.setReorderable(false);
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
    public void add(){
        try{
            this.viewDispatcher.navigateTo(Pages.ADD_DISCO, this.collezionista);
        } catch (ViewDispatcherException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void remove() {

        Disco disco = table.getSelectionModel().getSelectedItem().getDisco();
        queryJdbc.removeDisco(disco.getId());
        table.getItems().remove(table.getSelectionModel().getSelectedItem());

    }

    @FXML
    public void ricerca() {
        try {
            viewDispatcher.navigateTo(Pages.RICERCA, collezionista);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void goToHome() {
        try {
            viewDispatcher.navigateTo(Pages.HOME,collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
