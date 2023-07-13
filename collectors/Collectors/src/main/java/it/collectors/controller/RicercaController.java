package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.*;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RicercaController implements Initializable, DataInitializable<Collezionista> {

    Query_JDBC db = BusinessFactory.getImplementation();
    Collezionista collezionista;
    @FXML
    private ChoiceBox<String> flag;

    @FXML
    private TextField discoField;

    @FXML
    private TextField autoreField;


    protected class DiscoWrapper {
        private Disco disco;
        private Etichetta etichetta;
        private Genere genere;
        private List<Autore> autori;

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

        public String getAutori() {
            String concat = " ";
            for (Autore a : autori) {
                concat += a.getNome_dAutore()+" ";
            }
            return concat;
        }

    }

    @FXML
    private TableView<DiscoWrapper> risultatiRicerca;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> titolo;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> annoUscita;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> barcode;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> formato;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> statoConservazione;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> descrizione;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> etichetta;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> genere;

    @FXML
    private TableColumn<DischiController.DiscoWrapper, String> autore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista data) {
        collezionista = data;
        titolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        annoUscita.setCellValueFactory(new PropertyValueFactory<>("annoUscita"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        statoConservazione.setCellValueFactory(new PropertyValueFactory<>("statoConservazione"));
        descrizione.setCellValueFactory(new PropertyValueFactory<>("descrizioneConservazione"));
        etichetta.setCellValueFactory(new PropertyValueFactory<>("etichetta"));
        genere.setCellValueFactory(new PropertyValueFactory<>("genere"));
        autore.setCellValueFactory(new PropertyValueFactory<>("nomeAutore"));
        flag.getItems().addAll("Condivisi", "Privati", "Tutti");
        flag.setValue("Tutti");

    }

    @FXML
    public void filtra() {

        Boolean condivisi = null;

        if (flag.getValue().equals("Condivisi")) {
            condivisi = true;
        } else if (flag.getValue().equals("Privati")) {
            condivisi = false;
        } else if (flag.getValue().equals("Tutti")) {
            condivisi = null;
        }

        List<Disco> dischi = db.getRicercaDischiPerAutoreTitolo(autoreField.getText(), discoField.getText(), condivisi, collezionista.getId());

        List<DiscoWrapper> dischiWrapper = new ArrayList<>();

        for (Disco disco : dischi) {
            Etichetta e = db.getEtichetta(disco.getId());
            Genere g = db.getGenere(disco.getId());
            List<Autore> a = db.getAutoriDisco(disco.getId());

            DiscoWrapper discoWrapper = new DiscoWrapper(
                    disco,
                    e,
                    g,
                    a
            );
            dischiWrapper.add(discoWrapper);
        }

        for (DiscoWrapper w : dischiWrapper) {
            risultatiRicerca.getItems().add(w);
        }
    }

    @FXML
    public void goToHome() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.navigateTo(Pages.HOME, collezionista);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
