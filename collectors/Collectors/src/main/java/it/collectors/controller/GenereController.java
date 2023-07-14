package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GenereController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private TextField textField;

    @FXML
    private Label exceptionLabel;

    @FXML
    private Button addButton;

    private Runnable callback;

    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    private static final GenereController genereController = new GenereController();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.exceptionLabel.textProperty().set("");
        this.addButton.disableProperty().bind(textField.textProperty().isEmpty());
    }

    @Override
    public void initializeData(Collezionista data) {

    }


    @FXML
    private void addGenere(){
        if(queryJdbc.aggiuntaGenere(textField.getText())){
            this.exceptionLabel.textProperty().set("Genere aggiunto con successo");
            this.textField.clear();
            if (this.callback != null) {
                this.callback.run();
            }
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

    public void setUpdateCallback(Runnable callback) {
        this.callback = callback;
    }

    public static GenereController getGenereController() {
        return genereController;
    }

}
