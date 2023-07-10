package it.collectors.controller;

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
        try{
            //User user = this.userService.validate(username.getText(),password.getText());
            ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
            viewDispatcher.navigateTo(Pages.HOME);
            //viewDispatcher.loggedIn(user);
            //viewDispatcher.navigateTo(Pages.HOME, user);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void register() {
        try{
            //User user = this.userService.validate(username.getText(),password.getText());
            ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
            //viewDispatcher.loggedIn(user);
            //viewDispatcher.navigateTo(Pages.HOME, user);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}

