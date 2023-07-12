package it.collectors;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Connect_JDBC;
import it.collectors.business.jdbc.DatabaseImpl;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.ArrayList;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        boolean loginVeloce = true; // mettere true per skippare la pagina di login
        try {
            ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

            viewDispatcher.setStage(stage);
            if(loginVeloce) {
                try {
                    viewDispatcher.navigateTo(Pages.HOME, new Collezionista(1,"a@a.a","a"));
                } catch (ViewDispatcherException e) {
                    throw new RuntimeException(e);
                }
            }
            else viewDispatcher.showLogin();
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
