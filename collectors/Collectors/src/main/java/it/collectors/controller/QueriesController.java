package it.collectors.controller;

import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class QueriesController implements Initializable, DataInitializable<Collezionista>{
    @FXML
    private Button home;
    Collezionista collezionista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
    }
    @FXML
    private void goToHome(){
            ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
            try {
                viewDispatcher.navigateTo(Pages.HOME, collezionista);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    @FXML
    private void goToOperazione1 () throws IOException {
            Stage childStage= new Stage(); // nuova finestra
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/addCollezione.fxml")); //caricare questo
            Parent childScene = loader.load(); // carico nella scena l'fxml
            AddCollezioneController child = loader.getController();
            child.setCollezionista(collezionista);
            child.loadTable(); // prendo il controller del figlio per passargli il collezionista e permettere il caricamento
            childStage.resizableProperty().setValue(false);
            childStage.setTitle("Aggiungi collezione");
            childStage.setScene(new Scene(childScene)); // carico la scena nello stage
            childStage.initOwner((Stage) home.getScene().getWindow()); // setto lo stage di queries come padre di quello di addCollezione
            childStage.initModality(Modality.APPLICATION_MODAL); // ordino al figlio di bloccare gli stage dei suoi padri
            childStage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
            childStage.showAndWait(); // visualizzo lo stage del figlio, fino alla chiusura
            //stage.close();

            //stage.show();
            //Stage thisStage = (Stage) home.getScene().getWindow();
            /*thisStage.setOnCloseRequest(event -> {event.consume();});
            stage.setOnCloseRequest(windowEvent -> {
                thisStage.setOnCloseRequest(null);
                exitable=true;
            });*/




    }
    @FXML
    private void goToOperazione2 () {

    }
    @FXML
    private void goToOperazione3 () {

    }
    @FXML
    private void goToOperazione4 () {

    }
    @FXML
    private void goToOperazione5 () {

    }
    @FXML
    private void goToOperazione6 () {

    }
    @FXML
    private void goToOperazione7 () {

    }
    @FXML
    private void goToOperazione8 () {

    }
    @FXML
    private void goToOperazione9 () {

    }
    @FXML
    private void goToOperazione10 () {

    }
    @FXML
    private void goToOperazione11 () {

    }
    @FXML
    private void goToOperazione12 () {

    }
    @FXML
    private void goToOperazione13 () {

    }

}
