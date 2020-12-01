package pl.edu.agh.diskstalker;

// import from our code
import pl.edu.agh.diskstalker.connection.ConnectionProvider;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.sql.SQLException;

// javaFX imports
import javafx.application.Application;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.view.PopUpNotification;

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
        QueryExecutor.create("INSERT INTO Items (ItemID, Path, Type, Size) VALUES (1, '/home', 'root', '4233')");

        ConnectionProvider.close();
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
