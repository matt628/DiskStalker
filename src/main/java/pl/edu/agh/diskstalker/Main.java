package pl.edu.agh.diskstalker;

// import from our code

import javafx.application.Application;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.presenter.DeleteHandler;
import pl.edu.agh.diskstalker.view.PopUpNotification;

import java.sql.SQLException;

// javaFX imports

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
        runNotification();
        launch();
    }

    public static void runNotification(){ //this method is only a placeholder for code to use later
        // Notification code
        try {
            PopUpNotification.displayTray("DiskStalker", "Hi notification works");
        } catch (Exception e) {
            e.printStackTrace();
        }
//  Notification optional code to ensure proper working
//        if (SystemTray.isSupported()) {
//        } else {
//            System.err.println("System tray not supported!");
//        }
    }
}
