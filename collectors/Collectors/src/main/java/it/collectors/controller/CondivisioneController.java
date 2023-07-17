package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class CondivisioneController implements Initializable {
    Query_JDBC db;
    Collezione collezione;
    public void setCollezione(Collezione collezione) {
        this.collezione=collezione;
    }

    @FXML
    private TableView<Collezionista> condivisioneTable;
    @FXML
    private TableColumn<Collezionista, String> condivisioneColumn;
    @FXML
    private TextField nomeCollezionista;
    @FXML
    private Label erroreCollezionistaNonTrovato;

    @FXML
    private Label erroreCollezionistaGiaPresente;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        condivisioneColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        nomeCollezionista.setOnKeyPressed(event -> {
            erroreCollezionistaNonTrovato.setVisible(false);
            erroreCollezionistaGiaPresente.setVisible(false);
            if (event.getCode() == KeyCode.ENTER) aggiungiCondivisione(null);
        });
        condivisioneTable.setPlaceholder(new Label("Nessuno"));


    }

    public void loadTable() {
        db = BusinessFactory.getImplementation();
        condivisioneTable.getItems().setAll(db.getCollezionistiDaCondivisaCollezione(collezione.getId()));
    }

    public void rimuoviCondivisione() {
        Collezionista collezionista = condivisioneTable.getSelectionModel().getSelectedItem();
        db.rimuoviCondivisione(collezione.getId(), collezionista.getId());
        condivisioneTable.getItems().remove(collezionista);
    }

    public void aggiungiCondivisione(ActionEvent actionEvent) {
        if(!condivisioneTable.getItems().isEmpty())
            while(condivisioneTable.getItems().iterator().next().getNickname() != nomeCollezionista.getText()){
                erroreCollezionistaGiaPresente.setVisible(true);
                return;
            }
        Collezionista collezionista = db.getCollezionistaDaNickname(nomeCollezionista.getText());
        if (collezionista == null) {
            erroreCollezionistaNonTrovato.setVisible(true);
            return;
        }
        db.inserisciCondivisione(collezione.getId(), collezionista.getId());
        condivisioneTable.getItems().add(collezionista);
    }
}
