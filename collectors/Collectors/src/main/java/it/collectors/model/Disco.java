package it.collectors.model;

import java.util.Objects;

public class Disco {

    private final Integer id;

    private final String titolo;

    private final Integer annoUscita;

    private final String barcode;

    private final String formato;

    private final String statoConservazione;

    private final String descrizioneConservazione;



    public Disco(Integer id, String titolo, Integer annoUscita, String barcode, String formato,String statoConservazione, String descrizioneConservazione) {
        this.id = id;
        this.titolo = titolo;
        this.annoUscita = annoUscita;
        this.barcode = barcode;
        this.formato = formato;
        this.statoConservazione = statoConservazione;
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

    public String getStatoConservazione() {
        return statoConservazione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disco disco = (Disco) o;
        return Objects.equals(id, disco.id) && Objects.equals(titolo, disco.titolo) && Objects.equals(annoUscita, disco.annoUscita) && Objects.equals(barcode, disco.barcode) && Objects.equals(formato, disco.formato) && Objects.equals(statoConservazione, disco.statoConservazione) && Objects.equals(descrizioneConservazione, disco.descrizioneConservazione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titolo, annoUscita, barcode, formato, statoConservazione, descrizioneConservazione);
    }
}
