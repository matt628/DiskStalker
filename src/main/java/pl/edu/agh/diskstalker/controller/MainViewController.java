package pl.edu.agh.diskstalker.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.guice.provider.FXMLLoaderProvider;
import pl.edu.agh.diskstalker.presenter.FolderAnalyzerHandler;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;
import pl.edu.agh.diskstalker.presenter.TreeHandler;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Singleton
public class MainViewController {

    @FXML
    private Button addButton;

    @FXML
    private ListView<Root> folderListView;

    @FXML
    private TreeView<Item> folderTreeView;

    @FXML
    private ProgressBar progressBar;

    private Stage primaryStage;

    @Inject
    private FXMLLoaderProvider fxmlLoaderProvider;

    @Inject
    private TreeHandler treeHandler;

    @Inject
    private FolderAnalyzerHandler analyzerHandler;

    @Inject
    private FolderDetailsHandler detailsHandler;

    public MainViewController() {
    }

    public MainViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    @SuppressWarnings("Duplicates")
    private void initialize() {
        analyzerHandler.analyzeAll();
        analyzerHandler.loadDirectories();

        treeHandler.updateRootList();
        treeHandler.cleanTree();
        treeHandler.cleanProgressBar();

        folderListView.setOnMouseClicked(click -> {
            var currentItemSelected = folderListView.getSelectionModel()
                    .getSelectedItem();
            if (click.getClickCount() == 2) {
                showRootConfigurationDialog(currentItemSelected);
            } else if (click.getClickCount() == 1) {
                treeHandler.updateTree(currentItemSelected);
            }
        });
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            String pathname = selectedDirectory.getAbsolutePath();
            String name = pathname.substring(pathname.lastIndexOf(File.separator) + 1);
            String path = pathname.substring(0, pathname.lastIndexOf(File.separator));

            Root root = new Root(0, name, path, 0);

            showRootConfigurationDialog(root);

            analyzerHandler.analyzeRoot(root);
            analyzerHandler.addWatchDirectory(root);
            treeHandler.updateRootList();
        }
    }

    @FXML
    private boolean showRootConfigurationDialog(Root root) {
        try {
            //loading Pane
            FXMLLoader loader = fxmlLoaderProvider.get();
            loader.setLocation(MainViewController.class.getResource("/FolderDetails.fxml"));

            Stage dialogStage = createDialogStage(loader);

            FolderDetailsController folderDetailsController =
                    runFolderDetailsController(loader, dialogStage, root);

            dialogStage.showAndWait();
            return folderDetailsController.isApproved();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Stage createDialogStage(FXMLLoader loader) throws IOException {
        BorderPane page = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit root folder properties");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }

    private FolderDetailsController runFolderDetailsController(FXMLLoader loader, Stage dialogStage, Root root){
        FolderDetailsController folderDetailsController = loader.getController();
        folderDetailsController.setDialogStage(dialogStage);
        folderDetailsController.setRoot(root);
        return folderDetailsController;
    }

    public void updateFolderRootList(List<Root> roots) {
        folderListView.getItems().clear();
        folderListView.getItems().addAll(roots);
    }

    public void updateFolderTreeView(TreeItem<Item> root) {
        folderTreeView.setRoot(root);
        addContextMenuToTreeView();

    }

    public void updateProgressBarView(double progress){
        progressBar.setProgress(progress);
    }

    private void addContextMenuToTreeView() {
        ContextMenu contextMenu = new ContextMenu();
        folderTreeView.setOnMouseClicked(click -> {
            var currentItemSelected = folderTreeView.getSelectionModel()
                    .getSelectedItem();
            if (click.getButton() == (MouseButton.SECONDARY)) {
                showContextMenu(currentItemSelected, click.getScreenX(), click.getScreenY(), contextMenu);
            } else if (click.getButton() == (MouseButton.PRIMARY)) {
                contextMenu.hide();
            }
        });
    }

    private void showContextMenu(TreeItem<Item> item, double x, double y, ContextMenu contextMenu) {
        MenuItem menuItem1 = new MenuItem("Delete");
        menuItem1.setOnAction((event) -> this.detailsHandler.deleteItem(item.getValue()));
        contextMenu.getItems().setAll(menuItem1);
        contextMenu.show(this.folderTreeView.getScene().getWindow(), x, y);
    }
}
