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

    private Collezionista collezionista;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    Query_JDBC queryJdbc = BusinessFactory.getImplementation();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista data) {
        loggedInLabel.textProperty().set("Sei attualmente identificato come: "+data.getNickname());
        this.collezionista = data;
    }


    @FXML
    private void goToCollections() {
        try {
            viewDispatcher.navigateTo(Pages.COLLECTIONS, this.collezionista);
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToDisks() {
        try {
            viewDispatcher.navigateTo(Pages.DISCHI, this.collezionista);
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void goToTracks() {
        try {
            viewDispatcher.navigateTo(Pages.TRACCE, this.collezionista);
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void goToProfile() {
        try {
            viewDispatcher.navigateTo(Pages.PROFILO, this.collezionista);
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }
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
