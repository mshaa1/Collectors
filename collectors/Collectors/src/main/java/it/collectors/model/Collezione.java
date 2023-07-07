package it.collectors.model;

public class Collezione {

    private final Integer id;

    private final String nome;

    private Boolean flag;


    public Collezione(Integer id, String nome, Boolean flag) {
        this.id = id;
        this.nome = nome;
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
