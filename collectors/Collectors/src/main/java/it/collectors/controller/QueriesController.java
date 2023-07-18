package it.collectors.controller;

import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class QueriesController implements Initializable, DataInitializable<Collezionista>{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button home;

    private Collezionista collezionista;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

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
                viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Home", "home.fxml", this.collezionista);
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
    }

    @FXML
    private void goToOperazione2 () {

    }
    @FXML
    private void goToOperazione3 (){
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Collezioni", "collezioni.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione4 () {
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Collezioni", "collezioni.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione5 () {
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Collezioni", "collezioni.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione6 () {
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Home", "home.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione7 () {
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Home", "home.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione8 () {
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Dischi", "dischi.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione9 () throws IOException {
        Stage childStage= new Stage(); // nuova finestra
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/verificaVisibilita.fxml")); //caricare questo
        Parent childScene = loader.load(); // carico nella scena l'fxml
        VerificaVisibilitaController child = loader.getController();
        childStage.resizableProperty().setValue(false);
        childStage.setTitle("Verrifica visibilità");
        childStage.setScene(new Scene(childScene)); // carico la scena nello stage
        childStage.initOwner((Stage) home.getScene().getWindow()); // setto lo stage di queries come padre di quello di addCollezione
        childStage.initModality(Modality.APPLICATION_MODAL); // ordino al figlio di bloccare gli stage dei suoi padri
        childStage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        childStage.showAndWait(); // visualizzo lo stage del figlio, fino alla chiusura
    }
    @FXML
    private void goToOperazione10 () {

    }
    @FXML
    private void goToOperazione11 () {

    }
    @FXML
    private void goToOperazione12 () {
        try {
            viewDispatcher.changeStage(anchorPane.getScene(), anchorPane, "Profilo", "profilo.fxml", this.collezionista);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void goToOperazione13 () {

    }

}
