package it.collectors.model;

import java.sql.Time;

public class Traccia {

    private final Integer id;

    private final String titolo;

    private final Time durata;


    public Traccia(Integer id, String titolo, Time durata) {
        this.id = id;
        this.titolo = titolo;
        this.durata = durata;
    }

    public Integer getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public Time getDurata() {
        return durata;
    }

}
