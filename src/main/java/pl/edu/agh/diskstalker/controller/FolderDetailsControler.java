package pl.edu.agh.diskstalker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import pl.edu.agh.diskstalker.model.Root;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;
import pl.edu.agh.diskstalker.presenter.SoundEffects;

import java.io.IOException;

public class FolderDetailsControler {
    private Stage dialogStage;
    private Root root; //Finaly it should be Root
    private boolean approved;

    @FXML
    private TextField folderPath;


    public FolderDetailsControler() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRoot(Root root) {
        this.root = root;
        updateDisplay();
    }

    private void updateDisplay(){
        folderPath.setText(this.root.getPath());
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

        return;
    }

    @FXML
    private void  handleFolderDeleteAction(ActionEvent event) {
        try {
            FolderDetailsHandler.deleter(this.root.getPath());
            SoundEffects.playSound("delete_surprise.wav");
        } catch (IOException e) {
            e.printStackTrace();
//            TODO do sth with this exception
        }
        return;
    }


    @FXML
    private void handleFolderCleanAction(ActionEvent event) {
        try {
            FolderDetailsHandler.directoryCleaner(this.root.getPath());
            SoundEffects.playSound("delete_surprise.wav");
        } catch (IOException e) {
            e.printStackTrace();
//            TODO do sth with this exception
        }
        return;
    }
}
