package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
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

public class EditCollezioneController implements Initializable {
    @FXML
    private Label erroreNomeCollezioneLabel;
    private Collezionista collezionista;
    private Collezione collezione;
    private List<Disco> dischiPrimaDiModifica;
    protected void setCollezionista(Collezionista c){
        this.collezionista=c;
    }
    protected void setCollezione(Collezione c){
        this.collezione=c;
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
        nome.setText(collezione.getNome());
        Query_JDBC db = BusinessFactory.getImplementation();
        List<Disco> dischi = db.getDischiUtente(collezionista.getId());
        dischiTable.getItems().addAll(dischi);
        dischi = db.listaDischiCollezione(collezione.getId());
        dischiTable.getItems().removeAll(dischi);
        collezioneTable.getItems().addAll(dischi);
        dischiPrimaDiModifica = dischi;

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


    public void modificaCollezione(ActionEvent actionEvent) {
        if(nome.getText().isBlank()) {
            erroreNomeCollezioneLabel.setVisible(true);
            return;
        }
        Query_JDBC db = BusinessFactory.getImplementation();
        boolean f;
        if (flag.getValue().equals("Pubblica")) f = true;
        else f = false;

        db.editCollezione(new Collezione(collezione.getId(), nome.getText(), f));
        for(Disco d: dischiPrimaDiModifica) {
            db.rimozioneDiscoCollezione(collezione.getId(), d.getId());
        }
        for (Disco d: collezioneTable.getItems().stream().toList()) {
            db.inserimentoDiscoInCollezione(d.getId(), collezione.getId());
        }
        Stage stage =(Stage) erroreNomeCollezioneLabel.getScene().getWindow();
        stage.close();
    }
}
