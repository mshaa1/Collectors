package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionsController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private TableView<String> collectionsTable;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista data) {

    }

    @FXML
    public void addCollection() {

    }

    @FXML
    public void removeCollection() {

    }

    @FXML
    public void goToHome() {

    }

}
