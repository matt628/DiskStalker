package pl.edu.agh.diskstalker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class FolderDetailsControler {
    private Stage dialogStage;
    private boolean approved;


    public FolderDetailsControler() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
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
        // TODO: implement
        dialogStage.close();
    }

    @FXML
    private void  handleRootUnsubscribeAction(ActionEvent event) {
        return;
    }

    @FXML
    private void  handleFolderDeleteAction(ActionEvent event) {
        return;
    }

    @FXML
    private void  handleFolderContentDeleteAction(ActionEvent event) {
        return;
    }
}
