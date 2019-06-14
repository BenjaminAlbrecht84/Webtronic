import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import presenter.Presenter;
import view.View;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createNewWindow();
    }

    public void createNewWindow() {
        View view = new View();
        Model model = new Model();
        Presenter presenter = new Presenter(view, model);

        Stage stage = new Stage();
        stage.setScene(new Scene(view.getRoot()));
        stage.show();

        model.setPresenter(presenter);
        view.setPresenter(presenter);
        presenter.setMainStage(stage);
    }

}
