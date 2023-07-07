package it.collectors.view;

import it.collectors.controller.DataInitializable;
import javafx.scene.Parent;

public class View<T> {
    private DataInitializable<T> controller;
    private Parent view;

    public View(Parent view, DataInitializable<T> controller) {
        this.controller = controller;
        this.view = view;
    }

    public DataInitializable<T> getController() {
        return controller;
    }
    public Parent getView() {
        return view;
    }

}
