package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.ApplicationException;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.View;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    public Label loggedInLabel;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    Query_JDBC queryJdbc = BusinessFactory.getImplementation();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista data) {
        loggedInLabel.textProperty().set("Sei attualmente identificato come: "+data.getNickname());
    }


    @FXML
    private void goToCollections() {

    }

    @FXML
    private void goToDisks() {

    }

    @FXML
    private void goToTracks() {

    }

    @FXML
    private void goToProfile() {

    }

    @FXML
    private void logout() {
        try{
            queryJdbc.disconnect();
            viewDispatcher.showLogin();
        } catch (ApplicationException applicationException) {
            applicationException.printStackTrace();
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }
    }

}
