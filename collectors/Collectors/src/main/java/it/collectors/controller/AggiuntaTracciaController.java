package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.model.Traccia;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AggiuntaTracciaController implements Initializable, DataInitializable<Collezionista>{

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();
    private Collezionista collezionista;
    @FXML
    private TextField nomeTraccia;
    @FXML
    private TextField ore;
    @FXML
    private TextField minuti;
    @FXML
    private TextField secondi;
    @FXML
    private Label errore;

    @FXML
    private TableView<Disco> tabella;
    @FXML
    private TableColumn<Disco, String> dischi;

    ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista collezionista) {
        this.collezionista = collezionista;
        errore.setText("");
        dischi.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        tabella.getItems().addAll(queryJdbc.getDischiUtente(collezionista.getId()));
    }


    @FXML
    public void goToTracce() {
        try {
            viewDispatcher.navigateTo(Pages.TRACCE, collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void aggiungiTraccia() {
        int ore;
        int minuti;
        int secondi;
        ore = Integer.parseInt(this.ore.getText());
        minuti = Integer.parseInt(this.minuti.getText());
        secondi = Integer.parseInt(this.secondi.getText());
        if (minuti>59 || secondi>59) {
            errore.setText("Minuti e secondi devono essere inferiori a 60");
        }else {
            Disco d = tabella.getSelectionModel().getSelectedItem();
            if (d == null ){
                errore.setText("Selaziona un disco");
            }else {
                queryJdbc.inserimentoTracceInDisco(d.getId(), nomeTraccia.getText(), ore, minuti, secondi);
            }
        }
        errore.setText("");
        this.nomeTraccia.setText("");
        this.ore.setText("");
        this.minuti.setText("");
        this.secondi.setText("");
    }
}
