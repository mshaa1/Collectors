package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezione;
import it.collectors.model.Collezionista;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;


public class CollezioniCondiviseController implements Initializable, DataInitializable<Collezionista> {

    @FXML
    private TableView<CollezioneCollezionistaWrapper> tabella;

    @FXML
    private TableColumn<CollezioneCollezionistaWrapper, String> nomeCollezione;

    @FXML
    private TableColumn<CollezioneCollezionistaWrapper, String> nomeCollezionista;

    Query_JDBC db = BusinessFactory.getImplementation();



    protected class CollezioneCollezionistaWrapper{
        private Collezione collezione;
        private Collezionista collezionista;

        public CollezioneCollezionistaWrapper(Collezione collezione, Collezionista collezionista) {
            this.collezione = collezione;
            this.collezionista = collezionista;
        }

        public String getCollezione() {
            return collezione.getNome();
        }

        public String getCollezionista() {
            return collezionista.getNickname();
        }
    }

    private Collezionista collezionista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;

        nomeCollezione.setCellValueFactory(new PropertyValueFactory<>("collezione"));
        nomeCollezionista.setCellValueFactory(new PropertyValueFactory<>("collezionista"));

        List<CollezioneCollezionistaWrapper> collezioneCollezionistaWrappers = new ArrayList<>();
        Map<Collezione,Collezionista> map;

        map = db.getCollezioniCondiviseProprietario(this.collezionista.getId());

        for (Map.Entry<Collezione, Collezionista> entry : map.entrySet()) {
            collezioneCollezionistaWrappers.add(new CollezioneCollezionistaWrapper(entry.getKey(), entry.getValue()));
        }


        for (CollezioneCollezionistaWrapper c : collezioneCollezionistaWrappers) {
            tabella.getItems().add(c);
        }

    }

    @FXML
    public void goToCollezioni() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.navigateTo(Pages.COLLECTIONS, collezionista);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
