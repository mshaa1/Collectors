package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
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
    }

    public void initializeData() {

    }

    @FXML
    private void login() {

        String nickname = nicknameLabel.getText();
        String email = emailLabel.getText();

        Boolean accesso;
        accesso = queryJdbc.getAccesso(nickname,email);
        System.out.println(accesso);

        try{

            if(accesso){
                ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
                viewDispatcher.navigateTo(Pages.HOME);
            }else{
                exceptionLabel.textProperty().set("Nome utente o email sbagliati");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void register() {
        boolean registrazione;
        String nickname = nicknameLabel.getText();
        String email = emailLabel.getText();
        registrazione = queryJdbc.registrazioneUtente(nickname,email);
        try{
            if (registrazione){
                ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
                viewDispatcher.navigateTo(Pages.HOME);
            }else{
                exceptionLabel.textProperty().set("Nome utente o email sbagliati/utilizzati");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}

