package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddCollezioneController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private Label erroreNomeCollezioneLabel;
    private Collezionista collezionista;

    protected void setCollezionista(Collezionista c){
        this.collezionista=c;
    }

    @FXML
    private TextField nome;

    @FXML
    private ChoiceBox<String> flag;

    @FXML
    private TableView<Disco> dischiTable;

    @FXML
    private TableView<Disco> collezioneTable;


    @FXML
    private TableColumn<Disco, String> titoloDischiColumn;
    @FXML
    private TableColumn<Disco, String> barcodeDischiColumn;

    @FXML
    private TableColumn<Disco, String> titoloCollezioneColumn;
    @FXML
    private TableColumn<Disco, String> barcodeCollezioneColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        flag.getItems().addAll("Pubblica", "Privata");
        flag.setValue("Privata");
        titoloDischiColumn.setCellValueFactory(new PropertyValueFactory<>("Titolo"));
        barcodeDischiColumn.setCellValueFactory(new PropertyValueFactory<>("Barcode"));
        titoloCollezioneColumn.setCellValueFactory(new PropertyValueFactory<>("Titolo"));
        barcodeCollezioneColumn.setCellValueFactory(new PropertyValueFactory<>("Barcode"));
        titoloDischiColumn.setReorderable(false);
        barcodeDischiColumn.setReorderable(false);
        titoloCollezioneColumn.setReorderable(false);
        barcodeCollezioneColumn.setReorderable(false);
        nome.setOnKeyTyped(event -> {
            erroreNomeCollezioneLabel.setVisible(false);
        });
        collezioneTable.setPlaceholder(new Label("Nessun disco in collezione"));
        dischiTable.setPlaceholder(new Label("Nessun disco disponibile"));

    }

    protected void loadTable() {
        Query_JDBC db = BusinessFactory.getImplementation();
        List<Disco> dischi = db.getDischiUtente(collezionista.getId());
        dischiTable.getItems().addAll(dischi);
    }


    public void caricaInCollezione(ActionEvent actionEvent) {
        collezioneTable.getItems().add(dischiTable.getSelectionModel().getSelectedItem());
        dischiTable.getItems().remove(dischiTable.getSelectionModel().getSelectedItem());
    }


    public void rimuoviDaCollezione(ActionEvent actionEvent) {
        if (collezioneTable.getSelectionModel().getSelectedItem() == null) return;
        dischiTable.getItems().add(collezioneTable.getSelectionModel().getSelectedItem());
        collezioneTable.getItems().remove(collezioneTable.getSelectionModel().getSelectedItem());
    }


    public void aggiungiCollezione(ActionEvent actionEvent) {
        if(nome.getText().isBlank()) {
            erroreNomeCollezioneLabel.setVisible(true);
            return;
        }
        Query_JDBC db = BusinessFactory.getImplementation();
        boolean f;
        if (flag.getValue().equals("Pubblica")) f = true;
        else f = false;
        int idCollezione = db.inserimentoCollezione(nome.getText(), f, collezionista.getId());
        for (Disco d: collezioneTable.getItems().stream().toList()) {
            db.inserimentoDiscoInCollezione(d.getId(), idCollezione);
        }
        Stage stage =(Stage) erroreNomeCollezioneLabel.getScene().getWindow();
        stage.close();
    }
}
