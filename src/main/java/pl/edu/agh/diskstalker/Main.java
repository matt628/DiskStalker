package pl.edu.agh.diskstalker;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.controller.MainViewController;

import java.sql.SQLException;

public class Main extends Application {

    private Stage primaryStage;
    private MainViewController mainViewController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        this.mainViewController = new MainViewController(primaryStage);
        this.mainViewController.initRootLayout();



//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainPane.fxml"));
//        Label l = new Label("Hello, JavaFX " );
//        Scene scene = new Scene(root, 640, 480);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        this.appController = new pl.edu.agh.diskstalker.controller.DiskStalkerController(primaryStage);
//        this.appController.initRootLayout();
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}
