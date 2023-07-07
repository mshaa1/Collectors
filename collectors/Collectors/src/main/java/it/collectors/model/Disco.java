package it.collectors.model;

public class Disco {

    private final Integer id;

    private final String titolo;

    private final Integer annoUscita;

    private final String barcode;

    private final String formato;

    private final String descrizioneConservazione;


    public Disco(Integer id, String titolo, Integer annoUscita, String barcode, String formato, String descrizioneConservazione) {
        this.id = id;
        this.titolo = titolo;
        this.annoUscita = annoUscita;
        this.barcode = barcode;
        this.formato = formato;
        this.descrizioneConservazione = descrizioneConservazione;
    }

    public Integer getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public Integer getAnnoUscita() {
        return annoUscita;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getFormato() {
        return formato;
    }

    public String getDescrizioneConservazione() {
        return descrizioneConservazione;
    }

}
