package it.collectors.view;

public enum Pages {
    HOME("home"),
    COLLECTIONS("collezioni"),
    DISCHI("dischi"),
    TRACCE("tracce"),
    PROFILO("profilo"),
    RICERCA("ricerca"),
    ADD_DISCO("addDisco"),
    QUERIES("queries"),

    COLLEZIONICONDIVISE("collezioniCondivise");

    private final String name;

    Pages(String name) {
        this.name = name;
    }
    public String toString(){
        return name;
    }
}
