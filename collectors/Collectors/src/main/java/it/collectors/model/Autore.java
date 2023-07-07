package it.collectors.model;

import java.sql.Date;


public class Autore {

    private final Integer id;

    private final String nome;

    private final String cognome;

    private final Date dataNascita;

    private final String nome_dAutore;

    private final String info;

    private final String ruolo;


    public Autore(Integer id, String nome, String cognome, Date dataNascita, String nome_dAutore, String info, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.nome_dAutore = nome_dAutore;
        this.info = info;
        this.ruolo = ruolo;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public String getNome_dAutore() {
        return nome_dAutore;
    }

    public String getInfo() {
        return info;
    }

    public String getRuolo() {
        return ruolo;
    }

}
