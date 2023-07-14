package it.collectors.controller;

import com.mysql.cj.PreparedQuery;
import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.*;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfiloController implements Initializable, DataInitializable<Collezionista>{


    @FXML
    private Label nick;

    @FXML
    private Label email;

    @FXML
    private Label collezioniTotali;

    @FXML
    private Label minuti;

    @FXML
    private TableView<GenereNumeroDischi> table;

    @FXML
    private TableColumn<GenereNumeroDischi, String> genere;

    @FXML
    private TableColumn<GenereNumeroDischi, String> numero;


    Collezionista collezionista;
    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();

    protected class GenereNumeroDischi{
        private Genere genere;
        private int numero;

        public GenereNumeroDischi(Genere genere, int numeroDischi) {
            this.genere = genere;
            this.numero = numeroDischi;
        }
        public String getGenere() {
            return genere.getNome();
        }

        public int getNumero() {
            return numero;
        }

    }

    public int minutiTotaliSistema(){
        int minuti=0;
        List<Autore> autori = queryJdbc.getAutori();
        for(Autore a:autori){
            minuti = minuti + queryJdbc.getMinutiTotaliMusicaPerAutore(a.getId());
        }
        return minuti;
    }


    public List<GenereNumeroDischi> getGenereNumeroDischi(){
        Map<Genere, Integer> statistiche = queryJdbc.getStatisticheDischiPerGenere();
        List<GenereNumeroDischi> genereNumeroDischi = new ArrayList<>();

        for (Map.Entry<Genere, Integer> entry : statistiche.entrySet()) {
            genereNumeroDischi.add(new GenereNumeroDischi(entry.getKey(), entry.getValue()));
        }
        return genereNumeroDischi;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genere.setReorderable(false);
        numero.setReorderable(false);
    }

    @Override
    public void initializeData(Collezionista data) {
        this.collezionista = data;
        nick.setText(collezionista.getNickname());
        email.setText(collezionista.getEmail());
        minuti.setText(Integer.toString(minutiTotaliSistema()));
        collezioniTotali.setText(Integer.toString(queryJdbc.numeroCollezioniCollezionista(collezionista.getId())));

        genere.setCellValueFactory(new PropertyValueFactory<>("genere"));
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        for (GenereNumeroDischi gnd : getGenereNumeroDischi()) {
            table.getItems().add(gnd);
        }
    }


    @FXML
    public void goToHome() {
        ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();
        try {
            viewDispatcher.navigateTo(Pages.HOME, collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
