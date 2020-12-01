package pl.edu.agh.diskstalker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    private Stage primaryStage;

    @FXML
    private Button addButton;

    @FXML
    private ListView folderListView;

    public MainViewController(){}

    public MainViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("Disk Stalker");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainViewController.class.getResource("/MainPane.fxml"));
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
        //MOCK DATA
        List<String> rootFolders = new ArrayList<String>();
        rootFolders.add("myFirstPath");
        for(String folder : rootFolders) {
            folderListView.getItems().add(folder);
        }

    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        // FOLDER CHOOSER
        DirectoryChooser directoryChooser = new DirectoryChooser();

    }


}
