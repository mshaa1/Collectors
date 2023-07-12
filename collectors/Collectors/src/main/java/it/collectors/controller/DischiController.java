package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.model.Etichetta;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class DischiController implements Initializable, DataInitializable<Collezionista>{

    @FXML
    private TableView<Disco> table;

    @FXML
    private TableColumn<Disco, String> titolo;

    @FXML
    private TableColumn<Disco, String> annoUscita;

    @FXML
    private TableColumn<Disco, String> barcode;

    @FXML
    private TableColumn<Disco, String> formato;

    @FXML
    private TableColumn<Disco, String> statoConservazione;

    @FXML
    private TableColumn<Disco, String> descrizione;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    Collezionista collezionista;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        List<Disco> dischi = queryJdbc.getDischiUtente(data.getId());
        System.out.println(dischi);
        titolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        annoUscita.setCellValueFactory(new PropertyValueFactory<>("annoUscita"));
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        formato.setCellValueFactory(new PropertyValueFactory<>("formato"));
        statoConservazione.setCellValueFactory(new PropertyValueFactory<>("statoConservazione"));
        descrizione.setCellValueFactory(new PropertyValueFactory<>("descrizioneConservazione"));
        for(int i = 0; i < dischi.size(); i++) {
            table.getItems().add(dischi.get(i));
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
            viewDispatcher.navigateTo(Pages.HOME,collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
