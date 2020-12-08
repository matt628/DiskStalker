package pl.edu.agh.diskstalker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import pl.edu.agh.diskstalker.presenter.DeleteHandler;

import java.awt.*;
import java.io.IOException;

public class FolderDetailsControler {
    private Stage dialogStage;
    private String path; //Finaly it should be Root
    private boolean approved;

    @FXML
    private TextField maxSizeField;


    public FolderDetailsControler() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPath(String path) {
        this.path = path;
        updateDisplay();
    }

    private void updateDisplay(){
        maxSizeField.setText(this.path);
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
            DeleteHandler.deleter(this.path);
        } catch (IOException e) {
            e.printStackTrace();
//            TODO do sth with this exception
        }
        return;
    }


    @FXML
    private void  handleFolderContentDeleteAction(ActionEvent event) {
        return;
    }
}
