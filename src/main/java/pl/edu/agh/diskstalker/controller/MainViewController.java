package pl.edu.agh.diskstalker.controller;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import pl.edu.agh.diskstalker.Guice.GuiceModule;
import pl.edu.agh.diskstalker.model.Root;
import pl.edu.agh.diskstalker.presenter.SoundEffects;
import pl.edu.agh.diskstalker.presenter.TreeBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    private Stage primaryStage;

    @FXML
    private Button addButton;

    @FXML
    private ListView<Root> folderListView;

    @FXML
    private TreeView<String> folderTreeView;

    public MainViewController(){}

    public MainViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("Disk Stalker");
            Injector injector = Guice.createInjector(new GuiceModule());
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(instantiatedClass -> {
                return injector.getInstance(instantiatedClass);
            });
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

    void updateRoots(){
        folderListView.getItems().clear();
        for(Root folder : Root.findAll()) {
            folderListView.getItems().add(folder);
        }
    }

    @FXML
    @SuppressWarnings("Duplicates")
    //noinspection Duplicates
    private void initialize() {
        // MOCK DATA CREATION
        // root paths
        List<Root> rootFolders = Root.findAll();
        Root mockRoot = new Root(0, "myFirstPath", "C:\\Users\\Mateusz\\Desktop\\testowy", "", "" );
        rootFolders.add(mockRoot);

        // tree view
        //mock code
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
//        TreeBuilder treeBuilder = new TreeBuilder();
//        folderTreeView = treeBuilder.buildTree("D:\\Dokumenty2\\MEMY");



        // WORKING CODE
        for(Root folder : rootFolders) {
            folderListView.getItems().add(folder);
        }

        // handle double click on ListView item
        folderListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    var currentItemSelected = folderListView.getSelectionModel()
                            .getSelectedItem();
                    //use this to do whatever you want to. Open Link etc.
                    showRootConfigurationDialog(currentItemSelected);
                }
            }
        });

    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        // FOLDER CHOOSER
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        // ShowConfigData
        String path = selectedDirectory.getAbsolutePath();
        Root root = new Root(0,"", path, "", "");
        showRootConfigurationDialog(root);
        //TODO get max size and name
        Root.create("", path, "", "");
        SoundEffects.playSound("success.wav");
        updateRoots(); //TODO this should be in root.create()
//        folderListView.getItems().add(selectedDirectory.getAbsolutePath());
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
            dialogStage.setTitle("Edit root folder propeties");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FolderDetailsControler folderDetailsControler = loader.getController();
            folderDetailsControler.setDialogStage(dialogStage);
            folderDetailsControler.setRoot(root);

            dialogStage.showAndWait();
            return folderDetailsControler.isApproved();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
