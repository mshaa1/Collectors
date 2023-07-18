package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.*;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class DischiCoerentiController implements Initializable, DataInitializable<Collezionista> {

    Query_JDBC db = BusinessFactory.getImplementation();
    Collezionista collezionista;

    @FXML
    private VBox vBox;


    @FXML
    private TextField discoField;

    @FXML
    private TextField autoreField;

    @FXML
    private TextField barcodeField;


    protected class DiscoWrapper {
        private Disco disco;

        private Integer affinita;


        public DiscoWrapper(Disco disco, Integer affinita) {
            this.disco = disco;
            this.affinita = affinita;
        }

        public Disco getDisco() {
            return disco;
        }

        public String getTitolo() {
            return disco.getTitolo();
        }

        public String getAffinita() {
            return affinita.toString();
        }
    }

    @FXML
    private TableView<DiscoWrapper> risultatiRicerca;

    @FXML
    private TableColumn<DiscoWrapper, String> titolo;

    @FXML
    private TableColumn<DiscoWrapper, String> affinitaColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titolo.setReorderable(false);
        affinitaColumn.setReorderable(false);
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        titolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        affinitaColumn.setCellValueFactory(new PropertyValueFactory<>("affinita"));
    }

    private String regexify(String string) {
        if (string == null || string.isBlank()) return null;
        return "^.*"+string+".*$";
    }

    @FXML
    public void filtra() {

        if (risultatiRicerca!= null) {
            risultatiRicerca.getItems().clear();
        }

        String autore = regexify(autoreField.getText());
        String titolo = regexify(discoField.getText());
        String barcode = regexify(barcodeField.getText());

        Map<Disco, Integer> dischi = db.dischiSimiliA(barcode, titolo, autore);

        System.out.println(dischi.size());

        if (dischi == null) return;

        List<DiscoWrapper> dischiWrapper = new ArrayList<>();

        for (Disco d : dischi.keySet()) {
            dischiWrapper.add(new DiscoWrapper(d, dischi.get(d)));
        }

        for (DiscoWrapper w : dischiWrapper) {
            risultatiRicerca.getItems().add(w);
        }
    }

    @FXML
    public void goToHome() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.changeStage(vBox.getScene(), vBox, "Home", "home.fxml", this.collezionista);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
