package it.collectors.model;

import java.io.InputStream;

public class Immagine {

    private final Integer id;

    private final String filePath;

    private final String didascalia;

    private final InputStream content;

    private final byte[] cache;


    public Immagine(Integer id, String filePath, String descrizione, InputStream content, byte[] cache) {
        this.id = id;
        this.filePath = filePath;
        this.didascalia = descrizione;
        this.content = content;
        this.cache = cache;
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

    public InputStream getContent() {
        return content;
    }

    public byte[] getCache() {
        return cache;
    }

}
