package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Etichetta;
import it.collectors.model.Genere;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import it.collectors.view.ViewDispatcherException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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

    @FXML
    private VBox vBox;

    private Collezionista collezionista;

    private Set<Genere> generi = new HashSet<>();

    private Set<Etichetta> etichette = new HashSet<>();

    private Query_JDBC query_jdbc = BusinessFactory.getImplementation();

    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    private AddGenereController addGenereController;

    private AddEtichettaController addEtichettaController;

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

        System.gc();
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        popolamentoGenereComboBox();
        popolamentoEtichettaComboBox();
        popolamentoFormatoComboBox();
        popolamentoStatoConservazioneComboBox();
    }

    private void popolamentoGenereComboBox() {
        this.generi = query_jdbc.get_Generi_Sistema();
        genereComboBox.setItems(FXCollections.observableList(generi.stream().toList()));
    }

    private void popolamentoEtichettaComboBox() {
        this.etichette = query_jdbc.get_Etichette_Sistema();
        etichettaComboBox.setItems(FXCollections.observableList(etichette.stream().toList()));
    }

    private void popolamentoFormatoComboBox() {
        this.formatoComboBox.getItems().addAll("vinile", "cd", "digitale", "cassetta");
    }

    private void popolamentoStatoConservazioneComboBox() {
        this.statoConservazioneComboBox.getItems().addAll("nuovo", "come nuovo", "ottimo", "buono", "accettabile" );
    }

    @FXML
    private void addGenere() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/addGenere.fxml"));
        Parent root = loader.load();
        addGenereController = loader.getController();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle("Aggiungi genere");
        stage.setScene(new Scene(root));
        /*stage.setOnCloseRequest(event ->{
            popolamentoGenereComboBox();
        });*/
        stage.initOwner(this.addGenereButton.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        stage.showAndWait();
        popolamentoGenereComboBox();
    }

    @FXML
    private void addEtichetta() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/collectors/ui/views/addEtichetta.fxml"));
        Parent root = loader.load();
        addEtichettaController = loader.getController();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle("Aggiungi etichetta");
        stage.setScene(new Scene(root));
        /*stage.setOnCloseRequest(event ->{
            popolamentoEtichettaComboBox();
        });*/
        stage.initOwner(this.addEtichettaButton.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        stage.showAndWait();
        popolamentoEtichettaComboBox();
    }

    @FXML
    private void addDisk() {
        if(query_jdbc.inserisciDisco(this.collezionista.getId(),this.titoloDisco.getText(), Integer.parseInt(this.annoUscita.getText()), this.barcodeDisco.getText(),
                this.formatoComboBox.getValue(), this.statoConservazioneComboBox.getValue(),
                this.descrizioneConservazione.getText(), this.etichettaComboBox.getSelectionModel().getSelectedItem().getId(),
                this.genereComboBox.getSelectionModel().getSelectedItem().getId())) {
            this.exceptionLabel.textProperty().set("Disco inserito correttamente / incrementato il numero dei suoi duplicati");
        } else {
            this.exceptionLabel.textProperty().set("Errore nell'inserimento del disco");
        }
    }

    @FXML
    private void cancel() {
        viewDispatcher.changeStage(vBox.getScene(), vBox, "Dischi", "dischi.fxml", this.collezionista);
    }

}

