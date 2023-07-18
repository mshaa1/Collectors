package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Autore;
import it.collectors.model.Collezionista;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NumeroBraniAutoreController implements Initializable, DataInitializable<Collezionista>{

    @FXML
    private TableView<AutoreWrapper> tabella;

    @FXML
    private TableColumn<AutoreWrapper, String> autore;

    @FXML
    private TableColumn<AutoreWrapper, String> brano;

    protected class AutoreWrapper{
        private Autore autore;
        private int numeroBrani;

        public AutoreWrapper(Autore autore, int numeroBrani){
            this.autore=autore;
            this.numeroBrani=numeroBrani;
        }

        public String getNomeDAutore(){
            return this.autore.getNomeDAutore();
        }

        public String getNumeroBrani(){
            return String.valueOf(this.numeroBrani);
        }
    }

    private Query_JDBC db = BusinessFactory.getImplementation();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autore.setCellValueFactory(new PropertyValueFactory<>("nomeDAutore"));
        brano.setCellValueFactory(new PropertyValueFactory<>("numeroBrani"));
        List<Autore> autori = db.getAutori();

        for (Autore a : autori) {
            tabella.getItems().add(new AutoreWrapper(a, db.getNumeroTracceDistintePerAutoreCollezioniPubblice(a.getId())));
        }

    }
}
