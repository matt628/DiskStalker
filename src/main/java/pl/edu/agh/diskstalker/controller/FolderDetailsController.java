package pl.edu.agh.diskstalker.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import pl.edu.agh.diskstalker.model.Root;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;
import pl.edu.agh.diskstalker.presenter.SoundEffects;

import java.io.IOException;

public class FolderDetailsController {
    private Stage dialogStage;
    private Root root;
    private boolean approved;

    @FXML
    private TextField folderPath;

    @Inject
    private FolderDetailsHandler detailsHandler;

    public FolderDetailsController() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRoot(Root root) {
        this.root = root;
        updateDisplay();
    }

    private void updateDisplay(){
        folderPath.setText(root.getPath());
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        // TODO: implement
        try {
//            updateModel();
        } catch (Exception e) {
            System.out.println("An exception occurred " + e);
        }

        approved = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void  handleRootUnsubscribeAction(ActionEvent event) {
        detailsHandler.unsubscribeFromRoot(root);
        dialogStage.close();
    }

    @FXML
    private void  handleFolderDeleteAction(ActionEvent event) {
        try {
            detailsHandler.deleteRoot(root.getPath());
        } catch (IOException e) {
            e.printStackTrace();
//            TODO do sth with this exception
        }
    }


    @FXML
    private void handleFolderCleanAction(ActionEvent event) {
//        try {
////            FolderDetailsHandler.cleanRoot(root.getPath());
//            SoundEffects.playSound("delete_surprise.wav");
//        } catch (IOException e) {
//            e.printStackTrace();
////            TODO do sth with this exception
//        }
    }
}
