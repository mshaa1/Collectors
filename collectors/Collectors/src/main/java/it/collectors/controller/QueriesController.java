package it.collectors.controller;

import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class QueriesController implements Initializable, DataInitializable<Collezionista>{
    Collezionista collezionista;
    Stage stage=null;
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
            if (stage == null) {
                stage = new Stage();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/collectors/ui/views/addCollezione.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setUserData(collezionista);
            stage.resizableProperty().setValue(false);
            stage.setTitle("Aggiungi collezione");
            stage.setScene(scene);
            stage.show();

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
