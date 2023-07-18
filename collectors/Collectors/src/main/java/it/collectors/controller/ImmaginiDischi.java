package it.collectors.controller;

import it.collectors.business.BusinessFactory;
import it.collectors.business.jdbc.Query_JDBC;
import it.collectors.model.Collezionista;
import it.collectors.model.Disco;
import it.collectors.model.Immagine;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import it.collectors.view.Pages;
import it.collectors.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ImmaginiDischi<E> implements Initializable, DataInitializable<List<E>> {

    private Collezionista collezionista;
    private Disco disco;

    private int IDImmagine;

    @FXML
    private ImageView immagine;

    @FXML
    private Label didascalia;

    @FXML
    private Button indietro;
    @FXML
    private Button avanti;

    private List<Immagine> immagini;
    @FXML
    private Button didascaliaBottone;
    @FXML
    private TextField didascaliaText;
    @FXML
    private HBox didascaliaBox;
    @FXML
    private HBox immagineVisualizzata;


    private Query_JDBC queryJdbc = BusinessFactory.getImplementation();



    private ViewDispatcher viewDispatcher = ViewDispatcher.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(List<E> data) {
        immagineVisualizzata.setVisible(true);
        immagineVisualizzata.setManaged(true);
        didascaliaBox.setVisible(false);
        didascaliaBox.setManaged(false);

        this.disco = (Disco) data.get(0);
        this.collezionista = (Collezionista) data.get(1);



        didascalia.setText("");
        setImmagine();
    }

    @FXML
    public void aggiungi() {
        immagineVisualizzata.setVisible(false);
        immagineVisualizzata.setManaged(false);
        didascaliaBox.setVisible(true);
        didascaliaBox.setManaged(true);

    }

    @FXML
    public void inserisciDidascalia(){
        String didascalia = didascaliaText.getText();
        inserisciImmagine(didascalia);
    }


    private void inserisciImmagine(String didascalia) {
        javafx.scene.image.Image image = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona una immagine");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String imageUrl = file.toURI().toURL().toExternalForm();
                image = new Image(imageUrl);
                queryJdbc.addImmagineDisco(imageUrl, didascalia, disco.getId());
            } catch (MalformedURLException ex) {
                throw new IllegalStateException(ex);
            }
        }

        immagineVisualizzata.setVisible(true);
        immagineVisualizzata.setManaged(true);
        didascaliaBox.setVisible(false);
        didascaliaBox.setManaged(false);
        immagini = queryJdbc.getImmaginiDisco(disco.getId());

        this.didascalia.setText(didascalia);
        immagine.setImage(image);

        controlloFrecce();
    }

    public void setImmagine(){
        immagini = queryJdbc.getImmaginiDisco(disco.getId());
        if(immagini.size() > 0) {
            didascalia.setText(immagini.get(0).getDidascalia());
            immagine.setImage(new Image(immagini.get(0).getFilePath()));
            IDImmagine = immagini.get(0).getId();
        }else {
            didascalia.setText("Nessuna immagine");
            immagine.setImage(null);
        }
        controlloFrecce();
    }


    @FXML
    public void remove() {
        queryJdbc.removeImmagineDisco(IDImmagine);
        setImmagine();
    }

    public void controlloFrecce() {
        if(immagini.size() <= 1) {
            avanti.setVisible(false);
            indietro.setVisible(false);
        }else {
            avanti.setVisible(true);
            indietro.setVisible(true);
        }

    }

    @FXML
    public void goToDischi() {
        try {
            viewDispatcher.navigateTo(Pages.DISCHI,this.collezionista);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void avantiMouse() {
        int index = 0;
        for(int i = 0; i < immagini.size(); i++) {
            if(immagini.get(i).getFilePath().equals(immagine.getImage().getUrl())) {
                index = i;
            }
        }
        if(index == immagini.size() - 1) {
            index = 0;
        }else {
            index++;
        }
        didascalia.setText(immagini.get(index).getDidascalia());
        immagine.setImage(new Image(immagini.get(index).getFilePath()));
        IDImmagine = immagini.get(index).getId();

    }

    @FXML
    public void indietroMouse() {

        int index = 0;
        for(int i = 0; i < immagini.size(); i++) {
            if(immagini.get(i).getFilePath().equals(immagine.getImage().getUrl())) {
                index = i;
            }
        }
        if(index == 0) {
            index = immagini.size() - 1;
        }else {
            index--;
        }
        didascalia.setText(immagini.get(index).getDidascalia());
        immagine.setImage(new Image(immagini.get(index).getFilePath()));
        IDImmagine = immagini.get(index).getId();

    }

}
