package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CollezioneController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private VBox vBox;

    @FXML
    private TableView<Collezione> collezioneTable;

    @FXML
    private TableColumn<Collezione, String> collezioneNomeColonna;

    @FXML
    private TableColumn<Collezione, String> collezioneFlagColonna;

    @FXML
    private Label erroreSelezioneLabel;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    private Collezionista collezionista;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        collezioneNomeColonna.setReorderable(false);
        collezioneFlagColonna.setReorderable(false);
        collezioneTable.setOnMouseClicked(event -> {
            erroreSelezioneLabel.setVisible(false);
        });
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        List<Collezione> collezioni = queryJdbc.getCollezioniUtente(data.getId());
        collezioneNomeColonna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        collezioneFlagColonna.setCellValueFactory(new PropertyValueFactory<>("flag"));
        for(int i = 0; i < collezioni.size(); i++) {
            collezioneTable.getItems().setAll(collezioni);
        }
    }

    @FXML
    public void addCollection() throws IOException {
        Stage childStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/addCollezione.fxml"));
        Parent childScene = loader.load();
        AddCollezioneController child = loader.getController();
        child.setCollezionista(collezionista);
        child.loadTable();
        childStage.resizableProperty().setValue(false);
        childStage.setTitle("Aggiungi collezione");
        childStage.setScene(new Scene(childScene)); //
        childStage.initOwner((Stage) collezioneTable.getScene().getWindow()); //
        childStage.initModality(Modality.APPLICATION_MODAL); //
        childStage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        childStage.showAndWait();
        initializeData(collezionista);
    }

    @FXML
    public void editCollection() throws IOException{
        Collezione collezione = collezioneTable.getSelectionModel().getSelectedItem();
        if(collezione == null) {
            erroreSelezioneLabel.setVisible(true);
            return;
        }
        Stage childStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/editCollezione.fxml"));
        Parent childScene = loader.load();
        EditCollezioneController child = loader.getController();
        child.setCollezionista(collezionista);
        child.setCollezione(collezione);
        child.loadTable();
        childStage.resizableProperty().setValue(false);
        childStage.setTitle("Modifica collezione");
        childStage.setScene(new Scene(childScene)); //
        childStage.initOwner((Stage) collezioneTable.getScene().getWindow()); //
        childStage.initModality(Modality.APPLICATION_MODAL); //
        childStage.showAndWait();
        initializeData(collezionista);
    }

    @FXML
    public void toggleVisibilityCollection(){
        Query_JDBC db = BusinessFactory.getImplementation();
        Collezione collezione = collezioneTable.getSelectionModel().getSelectedItem();
        if(collezione == null){
            erroreSelezioneLabel.setVisible(true);
            return;
        }
        collezione.setFlag(!collezione.getFlag());
        db.modificaFlagCollezione(collezione.getId(), collezione.getFlag());
        collezioneTable.getItems().set(collezioneTable.getSelectionModel().getSelectedIndex(), collezione);
    }

    @FXML
    public void removeCollection() {
        Collezione collezione = collezioneTable.getSelectionModel().getSelectedItem();
        if (collezione == null) {
            erroreSelezioneLabel.setVisible(true);
        }
        queryJdbc.rimozioneCollezione(collezione.getId());
        collezioneTable.getItems().remove(collezione);
    }

    @FXML
    public void goToHome() {
        try {
            viewDispatcher.changeStage(vBox.getScene(), vBox, "Home", "home.fxml", this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void condividi(ActionEvent actionEvent) throws IOException {
        Collezione collezione = collezioneTable.getSelectionModel().getSelectedItem();
        if(collezione == null) {
            erroreSelezioneLabel.setVisible(true);
            return;
        }
        Stage childStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/condivisione.fxml"));
        Parent childScene = loader.load();
        CondivisioneController child = loader.getController();
        child.setCollezione(collezione);
        child.loadTable();
        childStage.resizableProperty().setValue(false);
        childStage.setTitle("Condividi");
        childStage.setScene(new Scene(childScene)); //
        childStage.initOwner((Stage) collezioneTable.getScene().getWindow()); //
        childStage.initModality(Modality.APPLICATION_MODAL); //
        childStage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        childStage.showAndWait();
    }

    public void condivise(){
        try {
            viewDispatcher.changeStage(vBox.getScene(), vBox, "Collezioni condivise", "collezioniCondivise.fxml", this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
