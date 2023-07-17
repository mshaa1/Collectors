package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.ApplicationException;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.model.Traccia;
import it.collectors.view.Pages;
import it.collectors.view.View;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private VBox vBox;

    @FXML
    public Label loggedInLabel;

    //TODO: fare un'altra tableview con le collezioni condivise con te
    @FXML
    private TableView<Collezione> tabellaCollezioni;

    @FXML
    private TableView<Disco> tabellaDischi;
    @FXML
    public TableView<Traccia> tabellaTracce;
    @FXML
    private TableColumn<Collezione, String> colonnaCollezioni;

    @FXML
    private TableColumn<Disco, String> colonnaDischi;

    @FXML TableColumn<Traccia,String> colonnaTracce;

    private Collezionista collezionista;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    Query_JDBC queryJdbc = BusinessFactory.getImplementation();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.gc();
    }

    @Override
    public void initializeData(Collezionista data) {
        loggedInLabel.textProperty().set("Sei attualmente identificato come: "+ data.getNickname());
        this.collezionista = data;
        colonnaCollezioni.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaDischi.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colonnaTracce.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colonnaDischi.setReorderable(false);
        colonnaCollezioni.setReorderable(false);
        colonnaTracce.setReorderable(false);
        Query_JDBC db = BusinessFactory.getImplementation();
        tabellaCollezioni.getItems().setAll(db.getCollezioniUtente(data.getId()));
        //TODO: evitare eccezione quando si seleziona una collezione con nessun disco
        tabellaCollezioni.setOnMouseClicked(event->{
            if(tabellaCollezioni.getSelectionModel().getSelectedItem()==null) return;
            tabellaDischi.getItems().setAll(db.listaDischiCollezione(tabellaCollezioni.getSelectionModel().getSelectedItem().getId()));
            tabellaTracce.getItems().setAll(new ArrayList<>());
        });
        //TODO: evitare eccezione quando si seleziona un disco con nessuna traccia
        tabellaDischi.setOnMouseClicked(event->{
            if(tabellaDischi.getSelectionModel().getSelectedItem()==null) return;
            tabellaTracce.getItems().setAll(db.tracklistDisco(tabellaDischi.getSelectionModel().getSelectedItem().getId()));
        });

    }


    @FXML
    private void goToCollections() {
        viewDispatcher.changeStage(vBox.getScene(), vBox, "Collezioni", "collezioni.fxml", this.collezionista);
    }

    @FXML
    private void goToDisks() {
        viewDispatcher.changeStage(vBox.getScene(), vBox, "Dischi", "dischi.fxml", this.collezionista);
    }

    @FXML
    private void goToTracks() {
        viewDispatcher.changeStage(vBox.getScene(), vBox, "Tracce", "tracce.fxml", this.collezionista);
    }

    @FXML
    private void goToProfile() {
        viewDispatcher.changeStage(vBox.getScene(), vBox, "Profilo", "profilo.fxml", this.collezionista);
    }
    @FXML
    private void goToQueries(){
        viewDispatcher.changeStage(vBox.getScene(), vBox, "Queries", "queries.fxml", this.collezionista);
    }

    @FXML
    private void logout() {
        try{
            queryJdbc.disconnect();
            viewDispatcher.showLogin();
            System.gc();
        } catch (ApplicationException applicationException) {
            applicationException.printStackTrace();
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }
    }

}
