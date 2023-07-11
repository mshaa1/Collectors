package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Connect_JDBC;
import it.collectors.business.jdbc.DatabaseImpl;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable, DataInitializable {

    @FXML
    private TextField nicknameLabel;

    @FXML
    private TextField emailLabel;

    @FXML
    private Label exceptionLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private VBox loginVBox;

    private Collezionista collezionista;

    Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loginButton
                .disableProperty()
                .bind(nicknameLabel
                        .textProperty()
                        .isEmpty()
                        .or(emailLabel.textProperty().isEmpty())
                );

        this.registerButton
                .disableProperty()
                .bind(this.loginButton.disableProperty());

        this.exceptionLabel.textProperty().set("");
    }

    public void initializeData() {

    }

    @FXML
    private void login() {
        if (queryJdbc.getConnection() == null) {
            queryJdbc = BusinessFactory.reOpenConnection();
        }

        String nickname = nicknameLabel.getText();
        String email = emailLabel.getText();

        Boolean accesso;
        Integer ID;
        accesso = queryJdbc.getAccesso(nickname,email);
        ID = queryJdbc.getIDUtente(nickname, email);

        this.collezionista = new Collezionista(ID, email, nickname);

        try{
            if(accesso){
                ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
                viewDispatcher.navigateTo(Pages.HOME, this.collezionista);
            }else{
                exceptionLabel.textProperty().set("Nome utente o email errati");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void register() {
        if (queryJdbc.getConnection() == null) {
            queryJdbc = BusinessFactory.reOpenConnection();
        }

        boolean registrazione;
        Integer ID;

        String nickname = nicknameLabel.getText();
        String email = emailLabel.getText();
        registrazione = queryJdbc.registrazioneUtente(nickname,email);
        ID = queryJdbc.getIDUtente(nickname, email);

        this.collezionista = new Collezionista(ID, email, nickname);

        try{
            if (registrazione){
                ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
                viewDispatcher.navigateTo(Pages.HOME, this.collezionista);
            }else{
                exceptionLabel.textProperty().set("Nome utente o email già in utilizzo");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}

