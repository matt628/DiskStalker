package pl.edu.agh.diskstalker.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class DiskStalkerController {
    private Stage primaryStage;
    public DiskStalkerController(){}

    public DiskStalkerController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("Disk Stalker");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DiskStalkerController.class.getResource("/MainPane.fxml"));
            HBox rootLayout = loader.load();
            //            BorderPane rootLayout = loader.load();

            // set initial data into controller
//            AccountOverviewController controller = loader.getController();
//            controller.setAppController(this);
//            controller.setData(DataGenerator.generateAccountData());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }

    }

    @FXML
    private void initialize() {

    }


}
