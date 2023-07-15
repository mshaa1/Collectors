package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Genere;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GenereController implements Initializable, DataInitializable<Genere> {

    @FXML
    private TextField textField;

    @FXML
    private Label exceptionLabel;

    @FXML
    private Button addButton;

    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.exceptionLabel.textProperty().set("");
        this.addButton.disableProperty().bind(textField.textProperty().isEmpty());

    }

    @Override
    public void initializeData(Genere data) {

    }


    @FXML
    private void addGenere(){
        if(queryJdbc.aggiuntaGenere(textField.getText())){
            this.exceptionLabel.textProperty().set("Genere aggiunto con successo");
            this.textField.clear();
        } else {
            this.exceptionLabel.textProperty().set("Genere già presente nel database");
        }
    }

    @FXML
    private void cancel() {
        try{
            Stage stage = (Stage) textField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            this.exceptionLabel.textProperty().set(e.getMessage());
        }
    }



}
