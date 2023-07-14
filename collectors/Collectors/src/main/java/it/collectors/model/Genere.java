package it.collectors.model;

public class Genere {

    private final Integer id;

    private final String nome;


    public Genere(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
    	if(obj instanceof Genere) {
    		Genere genere = (Genere) obj;
    		return genere.getNome().equals(this.getNome());
    	}
    	return false;
    }

}
