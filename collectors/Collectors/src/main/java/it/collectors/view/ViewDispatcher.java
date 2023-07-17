package it.collectors.view;


import it.collectors.controller.LoginController;
import it.collectors.model.Collezionista;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ViewDispatcher {

    private Stage stage;

    private static ViewDispatcher instance = new ViewDispatcher();

    static public ViewDispatcher getInstance(){
        return instance;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void loggedIn(Collezionista collezionista) throws ViewDispatcherException {
        validateStage();
        View<Collezionista> layoutPage = loadView("/it/collectors/ui/views/home.fxml");
        Parent layoutView = layoutPage.getView();
        layoutPage.getController().initializeData(collezionista);
        stage.setResizable(false);
        stage.setScene(new Scene(layoutView));
        navigateTo(Pages.HOME, collezionista);
    }

    public void showLogin() throws ViewDispatcherException {
        View loginPage = loadView("/it/collectors/ui/views/login.fxml");
        this.stage.setResizable(false);
        this.stage.setScene(new Scene(loginPage.getView()));
    }

    // Implementazione utilizzata solo per la pagina di login in quanto molto inefficiente
    public <T> void navigateTo(Pages page, T data) throws ViewDispatcherException {
        validateStage();
        View<T> viewToLoad = loadView("/it/collectors/ui/views/" + page.toString() + ".fxml");
        viewToLoad.getController().initializeData(data);
        this.stage.setResizable(false);
        this.stage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        this.stage.setScene(new Scene(viewToLoad.getView()));
    }

    /* Implementazione non più in utilizzo in quanto inutilizzata
    public void navigateTo(Pages page) throws ViewDispatcherException {
        validateStage();
        View viewToLoad = loadView("/it/collectors/ui/views/" + page.toString() + ".fxml");
        this.stage.getIcons().add(new Image("/it/collectors/ui/images/logo.png"));
        this.stage.setScene(new Scene(viewToLoad.getView())); //ci rendiamo conto di quanto sia inefficiente questa cosa
    }*/

    private void validateStage() throws ViewDispatcherException {
        if(stage == null){
            throw new ViewDispatcherException("Stage not set");
        }
    }

    private <T> View<T> loadView(String path) throws ViewDispatcherException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();
            return new View<>(view,loader.getController());
        }catch(Exception e){
            e.printStackTrace();
            throw new ViewDispatcherException(e.getMessage());
        }
    }

    public <T> void changeStage(Object obj, Parent event, String title, String url, T data){
        Platform.runLater(() -> {
            try {
                nullAll(obj, event);
                View<T> loader = loadView("/it/collectors/ui/views/" + url);
                loader.getController().initializeData(data);
                event.getScene().setRoot(loader.getView());
            } catch (ViewDispatcherException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void nullAll(Object obj, Parent root){
        ObservableList<Node> nodes = getAllViews(root);
        nodes.forEach(node -> node = null);
        nodes.clear();
        nodes = null;
        if(root instanceof StackPane){
            ((StackPane) root).getChildren().clear();
            root = null;

        }else{
            root = null;

        }
        obj = null;
        System.gc();
    }

    private static ObservableList<Node> getAllViews(Parent root) {
        ObservableList<Node> nodes = FXCollections.observableArrayList();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ObservableList<Node> nodes) {
        parent.getChildrenUnmodifiable().stream().map((node) -> {
            nodes.add(node);
            return node;
        }).map((node) -> {
            if (node instanceof Parent){
                addAllDescendents((Parent) node, nodes);

            }
            return node;
        }).map((node) -> {
            if(node instanceof ScrollPane){
                addAllDescendents((Parent) ((ScrollPane) node).getContent(), nodes);
                nodes.add(((ScrollPane) node).getContent());
            }
            return node;
        }).map((node) -> {
            if(node instanceof TabPane){
                ((TabPane) node).getTabs().forEach((t) -> {
                    addAllDescendents((Parent) ((Tab) t).getContent(), nodes);
                });
            }
            return node;
        });
    }

}
