package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class VerificaVisibilitaController implements Initializable {
    Query_JDBC db =  BusinessFactory.getImplementation();
    @FXML
    public TableView collezionistiTable;
    @FXML
    public TableColumn collezionistiColumn;
    @FXML
    public TableView collezioniTable;
    @FXML
    public TableColumn collezioniColumn;
    @FXML
    public Label visibileLabel;
    @FXML
    public Label nonVisibileLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        collezionistiColumn.setCellValueFactory(new PropertyValueFactory<Collezione, String>("nickname"));
        collezioniColumn.setCellValueFactory(new PropertyValueFactory<Collezione, String>("nome"));
        collezionistiTable.getItems().addAll(db.getCollezionisti());
        collezioniTable.getItems().addAll(db.getCollezioni());
        collezionistiTable.setOnMouseClicked(mouseEvent -> {
            verificaVisibilita();
        });
        collezioniTable.setOnMouseClicked(mouseEvent -> {
            verificaVisibilita();
        });
    }

    public void verificaVisibilita(){
        Collezione collezione = (Collezione) collezioniTable.getSelectionModel().getSelectedItem();
        Collezionista collezionista = (Collezionista) collezionistiTable.getSelectionModel().getSelectedItem();
        if (collezione == null || collezionista == null) return;
        if(db.getVerificaVisibilitaCollezione(collezione.getId(), collezionista.getId())){
            visibileLabel.setVisible(true);
            nonVisibileLabel.setVisible(false);
        }else
        {
            visibileLabel.setVisible(false);
            nonVisibileLabel.setVisible(true);
        }
    }
}
