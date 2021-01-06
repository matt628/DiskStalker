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
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }
}
