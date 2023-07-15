package it.collectors.view;

public enum Pages {
    HOME("home"),
    COLLECTIONS("collezioni"),
    DISCHI("dischi"),
    TRACCE("tracce"),
    PROFILO("profilo"),
    RICERCA("ricerca"),
    ADDDISCO("addDisco"),
    QUERIES("queries"),
    COLLEZIONICONDIVISE("collezioniCondivise"),
    ADDTRACK("aggiuntaTraccia");

    private final String name;

    Pages(String name) {
        this.name = name;
    }
    public String toString(){
        return name;
    }
}
