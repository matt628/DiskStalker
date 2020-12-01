package pl.edu.agh.diskstalker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    private Stage primaryStage;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> folderListView;

    @FXML
    private TreeView<String> folderTreeView;

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
    @SuppressWarnings("Duplicates")
    //noinspection Duplicates
    private void initialize() {
        // MOCK DATA CREATION
        // root paths
        List<String> rootFolders = new ArrayList<String>();
        rootFolders.add("myFirstPath");

        // tree view
        //main root
        TreeItem<String> mainRoot = new TreeItem<>("roots");

        //root path
        TreeItem<String> rootPath1 = new TreeItem<>("rootPath");
        mainRoot.getChildren().add(rootPath1);

        //add childs
        rootPath1.getChildren().add(new TreeItem<String>("1son of first"));
        TreeItem<String> son = new TreeItem<>("2son of first");
        rootPath1.getChildren().add(son);
        son.getChildren().add(new TreeItem<>("Son of a son"));

        //new rootpath
        TreeItem<String> rootPath2 = new TreeItem<>("rootPath");
        mainRoot.getChildren().add(rootPath2);


        folderTreeView.setRoot(mainRoot);



        // WORKING CODE
        for(String folder : rootFolders) {
            folderListView.getItems().add(folder);
        }

    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        // FOLDER CHOOSER
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        folderListView.getItems().add(selectedDirectory.getAbsolutePath());
    }

    @FXML
    private boolean showRootConfigurationDialog() {
        try {
            //loading Pane
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainViewController.class.getResource("/MainPane.fxml"));
            BorderPane page = loader.load();

            //creating dialog scene
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit root folder propeties");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FolderDetailsControler folderDetailsControler = loader.getController();
            folderDetailsControler.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            return folderDetailsControler.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
