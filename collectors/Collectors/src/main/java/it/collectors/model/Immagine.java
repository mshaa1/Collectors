package it.collectors.model;

import java.io.InputStream;

public class Immagine {

    private final Integer id;

    private final String filePath;

    private final String didascalia;


    public Immagine(Integer id, String filePath, String descrizione) {
        this.id = id;
        this.filePath = filePath;
        this.didascalia = descrizione;
    }

    public Integer getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDidascalia() {
        return didascalia;
    }

}
