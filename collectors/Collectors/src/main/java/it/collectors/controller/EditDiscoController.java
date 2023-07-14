package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.model.Etichetta;
import it.collectors.model.Genere;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditDiscoController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private TextField titoloDisco;

    @FXML
    private TextField annoUscita;

    @FXML
    private TextField barcodeDisco;

    @FXML
    private TextField descrizioneConservazione;

    @FXML
    private ComboBox<String> statoConservazioneComboBox;

    @FXML
    private ComboBox<Genere> genereComboBox;

    @FXML
    private ComboBox<String> formatoComboBox;

    @FXML
    private ComboBox<Etichetta> etichettaComboBox;

    @FXML
    private Button addGenereButton;

    @FXML
    private Button addEtichettaButton;

    @FXML
    private Button cancel;

    @FXML
    private Button addDiskButton;

    @FXML
    private Label exceptionLabel;

    private Collezionista collezionista;

    private List<Genere> generi = new ArrayList<>();

    private List<Etichetta> etichette = new ArrayList<>();

    private Query_JDBC query_jdbc = BusinessFactory.getImplementation();

    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    private Stage stage;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.addDiskButton.disableProperty().bind(titoloDisco.textProperty().isEmpty()
                .and(annoUscita.textProperty().isEmpty())
                .or(barcodeDisco.textProperty().isEmpty())
                .or(descrizioneConservazione.textProperty().isEmpty())
                .and(statoConservazioneComboBox.valueProperty().isNull())
                .and(genereComboBox.valueProperty().isNull())
                .and(formatoComboBox.valueProperty().isNull())
                .and(etichettaComboBox.valueProperty().isNull()));

        this.exceptionLabel.textProperty().set("");
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        popolamentoGenereComboBox();
        popolamentoEtichettaComboBox();
        popolamentoFormatoComboBox();
        popolamentoStatoConservazioneComboBox();
    }

    //TODO: implementare il metodo
    private void popolamentoGenereComboBox() {

    }

    //TODO: implementare il metodo
    private void popolamentoEtichettaComboBox() {

    }

    private void popolamentoFormatoComboBox() {
        this.formatoComboBox.getItems().addAll("vinile", "cd", "digitale", "cassetta");
    }

    private void popolamentoStatoConservazioneComboBox() {
        this.statoConservazioneComboBox.getItems().addAll("nuovo", "come nuovo", "ottimo", "buono", "accettabile" );
    }

    @FXML
    private void addGenere() {
        try{
            if (stage == null) {
                stage = new Stage();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/collectors/ui/views/addGenere.fxml"));
            Scene scene = new Scene(loader.load(), 300,300);
            stage.resizableProperty().setValue(false);
            stage.setTitle("Aggiungi genere");
            stage.setScene(scene);
            stage.show();

            GenereController controller = GenereController.getGenereController();
            controller.setUpdateCallback(() -> {
                this.genereComboBox.getItems().clear();
                popolamentoGenereComboBox();
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void addEtichetta() {
        try{
            if (stage == null) {
                stage = new Stage();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/collectors/ui/views/addEtichetta.fxml"));
            Scene scene = new Scene(loader.load(), 300,300);
            stage.resizableProperty().setValue(false);
            stage.setTitle("Aggiungi etichetta");
            stage.setScene(scene);
            stage.show();

            EtichettaController controller = EtichettaController.getEtichettaController();
            controller.setUpdateCallback(() -> {
                this.etichettaComboBox.getItems().clear();
                popolamentoEtichettaComboBox();
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void addDisk() {
        query_jdbc.inserisciDisco(this.titoloDisco.getText(), Integer.parseInt(this.annoUscita.getText()), this.barcodeDisco.getText(),
                this.formatoComboBox.getValue(), this.statoConservazioneComboBox.getValue(),
                this.descrizioneConservazione.getText(), this.etichettaComboBox.getValue().getId(),
                this.genereComboBox.getValue().getId());
    }

    @FXML
    private void cancel() {
        try{
            viewDispatcher.navigateTo(Pages.DISCHI, this.collezionista);
        } catch (ViewDispatcherException e) {
            throw new RuntimeException(e);
        }
    }

}

