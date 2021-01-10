package pl.edu.agh.diskstalker.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;

public class FolderDetailsController {
    private Stage dialogStage;
    private Root root;
    private boolean approved;

    @FXML
    private TextField folderPath;

    @FXML
    private TextField folderMaxSize;

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
        folderPath.setText(root.getPathname());
        //folderMaxSize.setText(getMaxSize()); //todo
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        long maxSize = 0;
        if (!folderMaxSize.getText().equals(""))
             maxSize = Long.parseLong(folderMaxSize.getText());
        detailsHandler.updateRoot(root.getName(), root.getPath(), maxSize);
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
        detailsHandler.deleteRoot(root.getPath());
        dialogStage.close();
    }

    @FXML
    private void handleFolderCleanAction(ActionEvent event) {
        detailsHandler.cleanRoot(root.getPath());
        dialogStage.close();
    }
}
