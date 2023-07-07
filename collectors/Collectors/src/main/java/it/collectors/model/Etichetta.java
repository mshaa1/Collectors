package it.collectors.model;

public class Etichetta {

    private final Integer id;

    private final String nome;

    private final String sedeLegale;

    private final String email;


    public Etichetta(Integer id, String nome, String sedeLegale, String email) {
        this.id = id;
        this.nome = nome;
        this.sedeLegale = sedeLegale;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSedeLegale() {
        return sedeLegale;
    }

    public String getEmail() {
        return email;
    }

}
