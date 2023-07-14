package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddCollezioneController implements Initializable, DataInitializable<Collezionista> {
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

    }

    @Override
    public void initializeData(Collezionista data) {
        //Window.
        Query_JDBC db = BusinessFactory.getImplementation();
        //List<Disco> dischi = db.getDischiUtente()
    }

    @FXML
    public void inserisci(){

    }
}
