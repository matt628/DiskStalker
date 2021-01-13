package pl.edu.agh.diskstalker.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;

import javax.swing.text.html.Option;
import java.util.Optional;

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
        folderMaxSize.setText(String.valueOf(root.getMaxSize()));
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
        var result  = showAlert("Folder will be deleted!", "Are you sure?");
        if (result.get() == ButtonType.OK){
            detailsHandler.deleteRoot(root);
        } else {
            // ... user chose CANCEL or closed the dialog
            dialogStage.close();
        }

        dialogStage.close();
    }

    @FXML
    private void handleFolderCleanAction(ActionEvent event) {
        var result  = showAlert("Folder content will be deleted!", "Are you sure?");
        if (result.get() == ButtonType.OK){
            detailsHandler.cleanRoot(root);
        } else {
            // ... user chose CANCEL or closed the dialog
            dialogStage.close();
        }
        dialogStage.close();
    }

    private Optional<ButtonType>  showAlert(String message,  String details){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText(details);

        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }
}
