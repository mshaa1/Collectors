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
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RicercaController implements Initializable, DataInitializable<Collezionista> {

    Query_JDBC db = BusinessFactory.getImplementation();
    Collezionista collezionista;

    @FXML
    private VBox vBox;

    @FXML
    private CheckBox pubblicheCheck;

    @FXML
    private CheckBox condiviseCheck;

    @FXML
    private CheckBox privateCheck;

    @FXML
    private TextField discoField;

    @FXML
    private TextField autoreField;


    protected class DiscoWrapper {
        private Disco disco;
        private Etichetta etichetta;
        private Genere genere;
        private List<Autore> autori;

        protected DiscoWrapper(Disco disco, Etichetta etichetta, Genere genere, List<Autore> autori) {
            this.disco = disco;
            this.etichetta = etichetta;
            this.genere = genere;
            this.autori = autori;
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

    @FXML
    private TableColumn<DiscoWrapper, String> autore;

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
        autore.setReorderable(false);
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
        autore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        privateCheck.setSelected(true); // per default cerco le private
        privateCheck.setAllowIndeterminate(false);
        pubblicheCheck.setAllowIndeterminate(false); // impedisco di avere il quadratino nel check
        condiviseCheck.setAllowIndeterminate(false);
        //flag.setOnAction(event -> {filtra();});
    }

    public static String regexify(String string) {
        if (string == null || string.isBlank()) return null;
        return "^.*"+string+".*$";
    }

    @FXML
    public void filtra() {

        if (risultatiRicerca!= null) {
            risultatiRicerca.getItems().clear();
        }

        Boolean pubbliche = pubblicheCheck.isSelected();
        Boolean condivise = condiviseCheck.isSelected();
        Boolean private_ = privateCheck.isSelected();

        String autore = regexify(autoreField.getText());
        String titolo = regexify(discoField.getText());

        List<Disco> dischi = db.getRicercaDischiPerAutoreTitolo(autore, titolo, pubbliche, condivise, private_, collezionista.getId());

        if (dischi==null) return;

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
    public void goToDischi() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.changeStage(vBox.getScene(), vBox, "Dischi", "dischi.fxml", this.collezionista);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
