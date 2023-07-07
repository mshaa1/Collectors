package it.collectors;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Connect_JDBC;
import it.collectors.business.jdbc.DatabaseImpl;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.view.ViewDispatcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    ViewDispatcher dispatcher = ViewDispatcher.getInstance();

    @Override
    public void start(Stage stage) throws Exception {


        /*
        try {
            ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
            viewDispatcher.setStage(stage);
            viewDispatcher.showLogin();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore");
        }*/
    }
}
