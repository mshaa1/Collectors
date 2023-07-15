package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CollezioneController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private TableView<Collezione> collezioneTable;

    @FXML
    private TableColumn<Collezione, String> collezioneNomeColonna;

    @FXML
    private TableColumn<Collezione, String> collezioneFlagColonna;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    private Collezionista collezionista;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        collezioneNomeColonna.setReorderable(false);
        collezioneFlagColonna.setReorderable(false);
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
        childStage.showAndWait();
        initializeData(collezionista);
    }

    @FXML
    public void removeCollection() {
        Collezione collezione = collezioneTable.getSelectionModel().getSelectedItem();
        queryJdbc.removeCollezione(collezione.getId());
        collezioneTable.getItems().remove(collezione);

    }

    @FXML
    public void goToHome() {
        try {
            viewDispatcher.navigateTo(Pages.HOME, this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void condivise(){
        try {
            viewDispatcher.navigateTo(Pages.COLLEZIONICONDIVISE, this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
