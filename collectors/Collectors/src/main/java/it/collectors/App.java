package it.collectors;

import it.collectors.business.jdbc.Connect_JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

    }

    public static void main(String[] args) {
        launch(args);
    }
}