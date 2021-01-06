package pl.edu.agh.diskstalker.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;
import pl.edu.agh.diskstalker.presenter.FolderAnalyzerHandler;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;
import pl.edu.agh.diskstalker.presenter.SoundEffects;
import pl.edu.agh.diskstalker.presenter.TreeHandler;

import java.io.File;
import java.util.List;

public class MainViewController {

    @FXML
    private Button addButton;

    @FXML
    private ListView<Root> folderListView;

    @FXML
    private TreeView<Item> folderTreeView;

    private Stage primaryStage;

    @Inject
    private TreeHandler treeHandler;

    @Inject
    private FolderAnalyzerHandler analyzerHandler;

    public MainViewController() {
    }

    public MainViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    @SuppressWarnings("Duplicates")
    //noinspection Duplicates
    private void initialize() {
        treeHandler.updateRootList();

        // handle clicks on ListView item
        folderListView.setOnMouseClicked(click -> {
            var currentItemSelected = folderListView.getSelectionModel()
                    .getSelectedItem();
            if (click.getClickCount() == 2) {
                showRootConfigurationDialog(currentItemSelected);
            } else if (click.getClickCount() == 1) {
                analyzerHandler.analyzeRoot(currentItemSelected);
                treeHandler.buildTree(currentItemSelected);
            }
        });

    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            String pathname = selectedDirectory.getAbsolutePath();
            System.out.println(pathname);
            String name = pathname.substring(pathname.lastIndexOf(File.separator) + 1);
            System.out.println(name);
            String path = pathname.substring(0, pathname.lastIndexOf(File.separator));
            System.out.println(path);
            Root root = new Root(0, "", path, 0);
            showRootConfigurationDialog(root);
            //TODO get max size
            Root.create(name, path, "0");
            SoundEffects.playSound("success.wav");
            treeHandler.updateRootList(); //TODO this should be in root.create()
        }
    }

    @FXML
    private boolean showRootConfigurationDialog(Root root) {
        try {
            //loading Pane
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainViewController.class.getResource("/FolderDetails.fxml"));
            BorderPane page = loader.load();

            //creating dialog scene
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit root folder properties");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FolderDetailsController folderDetailsController = loader.getController();
            folderDetailsController.setDialogStage(dialogStage);
            folderDetailsController.setRoot(root);

            dialogStage.showAndWait();
            return folderDetailsController.isApproved();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateFolderRootList(List<Root> roots) {
        folderListView.getItems().clear();
        folderListView.getItems().addAll(roots);
    }

    public void updateFolderTreeView(TreeItem<Item> root) {
        folderTreeView.setRoot(root);
    }
}
