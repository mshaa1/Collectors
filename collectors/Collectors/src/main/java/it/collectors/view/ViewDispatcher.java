package it.collectors.view;


import it.collectors.model.Collezionista;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
        stage.setScene(new Scene(layoutView));
        navigateTo(Pages.HOME, collezionista);
    }

    public void showLogin() throws ViewDispatcherException {
        View loginPage = loadView("/it/collectors/ui/views/login.fxml");
        this.stage.setScene(new Scene(loginPage.getView()));
    }

    public <T> void navigateTo(Pages page, T data) throws ViewDispatcherException {
        validateStage();
        View<T> viewToLoad = loadView("/it/collectors/ui/views/" + page.toString() + ".fxml");
        viewToLoad.getController().initializeData(data);
        this.stage.setScene(new Scene(viewToLoad.getView())); //ci rendiamo conto di quanto sia inefficiente questa cosa
    }

    public void navigateTo(Pages page) throws ViewDispatcherException {
        validateStage();
        View viewToLoad = loadView("/it/collectors/ui/views/" + page.toString() + ".fxml");
        this.stage.setScene(new Scene(viewToLoad.getView())); //ci rendiamo conto di quanto sia inefficiente questa cosa
    }

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

}
