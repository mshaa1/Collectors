package it.collectors.view;

public enum Pages {
    HOME("home");


    private final String name;

    Pages(String name) {
        this.name = name;
    }
    public String toString(){
        return name;
    }
}
