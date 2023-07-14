package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Etichetta;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ResourceBundle;

public class EtichettaController implements Initializable, DataInitializable<Collezionista>{

    @FXML
    private TextField nomeField;

    @FXML
    private TextField sedeField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addButton;

    @FXML
    private Label exceptionLabel;

    private Runnable callback;

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    private static final EtichettaController etichettaController = new EtichettaController();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.exceptionLabel.textProperty().set("");
        this.addButton.disableProperty().bind(nomeField.textProperty().isEmpty()
                        .and(sedeField.textProperty().isEmpty()
                        .and(emailField.textProperty().isEmpty())));
    }

    @Override
    public void initializeData(Collezionista data) {

    }


    @FXML
    private void addEtichetta() {
        if(queryJdbc.inserimentoEtichetta(nomeField.getText(), sedeField.getText(), emailField.getText())){
            this.exceptionLabel.textProperty().set("Etichetta aggiunta con successo");
            clearFields();
            if (this.callback != null) {
                this.callback.run();
            }
        } else {
            this.exceptionLabel.textProperty().set("Etichetta già presente nel database o dati non validi");
        }
    }

    @FXML
    public void cancel() {
        try{
            Stage stage = (Stage) nomeField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            this.exceptionLabel.textProperty().set(e.getMessage());
        }
    }

    public void setUpdateCallback(Runnable callback) {
        this.callback = callback;
    }

    private void clearFields() {
        nomeField.clear();
        sedeField.clear();
        emailField.clear();
    }

    public static EtichettaController getEtichettaController() {
    	return etichettaController;
    }

}
