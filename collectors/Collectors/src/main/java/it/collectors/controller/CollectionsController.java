package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionsController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private TableView<Collezione> collectionsTable;

    @FXML
    private TableColumn<Collezione, String> collectionNameColumn;

    @FXML
    private TableColumn<Collezione, String> collectionFlagColumn;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    Collezionista collezionista;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        collectionNameColumn.setReorderable(false);
        collectionFlagColumn.setReorderable(false);
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        List<Collezione> collezioni = queryJdbc.getCollezioniUtente(data.getId());
        collectionNameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        collectionFlagColumn.setCellValueFactory(new PropertyValueFactory<>("flag"));
        for(int i = 0; i < collezioni.size(); i++) {
            collectionsTable.getItems().add(collezioni.get(i));
        }
    }

    @FXML
    public void addCollection() {


    }

    @FXML
    public void removeCollection() {

    }

    @FXML
    public void goToHome() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.navigateTo(Pages.HOME, this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
