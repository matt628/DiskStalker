package pl.edu.agh.diskstalker.controller;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.presenter.FolderDetailsHandler;

import java.util.Optional;


public class FolderDetailsController {


    private Stage dialogStage;
    private Root root;
    private boolean approved;

    @FXML
    private TextField folderPath;

    @FXML
    private TextField folderMaxSize;

    @FXML
    private TextField folderMaxFilesNumber;

    @FXML
    private TextField folderMaxFileSize;

    @FXML
    private ComboBox<String> folderMaxSizeUnit;

    @FXML
    private ComboBox<String> folderMaxFileSizeUnit;

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

    private void updateDisplay() {
        folderPath.setText(root.getPathname());
        String folderMaxSizeReadable = bytesToReadableAndUpdateCombobox(root.getMaxSize(), folderMaxSizeUnit);
        folderMaxSize.setText(folderMaxSizeReadable);

        folderMaxFilesNumber.setText(String.valueOf(root.getMaxTreeSize()));

        // convert to readable
        String folderMaxFileSizeReadable = bytesToReadableAndUpdateCombobox(root.getMaxFileSize(), folderMaxFileSizeUnit);
        folderMaxFileSize.setText(folderMaxFileSizeReadable);


    }

    private String bytesToReadableAndUpdateCombobox(long sizeLong, ComboBox<String> comboBox) {
        double size = (double) sizeLong;
        String displaySize = "";
        if (size / 1073741824.0 > 1) {
            displaySize = String.valueOf(size / 1073741824.0);
            comboBox.setValue("GB");
        } else if (size / 1048576.0 > 1) {
            displaySize = String.valueOf(size / 1048576.0);
            comboBox.setValue("MB");
        } else if (size / 1024.0 > 1) {
            displaySize = String.valueOf(size / 1024.0);
            comboBox.setValue("kB");
        }
        return displaySize;
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        long maxSize = Long.MAX_VALUE;
        if (folderMaxSize != null && !folderMaxSize.getText().isEmpty()) {
            String unit = folderMaxSizeUnit.getSelectionModel().selectedItemProperty().getValue();
            maxSize = stringSizeToLong(folderMaxSize.getText(), unit);
        }

        long maxFilesNumber = Long.MAX_VALUE;
        if (folderMaxFilesNumber != null && !folderMaxFilesNumber.getText().isEmpty() )
            maxFilesNumber = Long.parseLong(folderMaxFilesNumber.getText());

        long maxFileSize = Long.MAX_VALUE;
        if (folderMaxFileSize != null && !folderMaxFileSize.getText().isEmpty()){
            String unit = folderMaxFileSizeUnit.getSelectionModel().selectedItemProperty().getValue();
            maxFileSize = stringSizeToLong(folderMaxFileSize.getText(), unit);
        }

        detailsHandler.updateRoot(root.getName(), root.getPath(), maxSize, maxFilesNumber, maxFileSize);
        approved = true;
        dialogStage.close();
    }

    long stringSizeToLong(String size, String unit) {
        size = size.replaceAll(",", ".");
        double maxSize = Double.parseDouble(size);
        maxSize = switch (unit) {
            case "GB" -> maxSize * 1073741824;
            case "MB" -> maxSize * 1048576;
            case "kB" -> maxSize * 1024;
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
        return (long) maxSize;
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleRootUnsubscribeAction(ActionEvent event) {
        detailsHandler.unsubscribeFromRoot(root);
        dialogStage.close();
    }

    @FXML
    private void handleFolderDeleteAction(ActionEvent event) {
        var result = showAlert("Folder will be deleted!", "Are you sure?");
        if (result.get() == ButtonType.OK) {
            detailsHandler.deleteRoot(root);
        } else {
            dialogStage.close();
        }

        dialogStage.close();
    }

    @FXML
    private void handleFolderCleanAction(ActionEvent event) {
        var result = showAlert("Folder content will be deleted!", "Are you sure?");
        if (result.get() == ButtonType.OK) {
            detailsHandler.cleanRoot(root);
        } else {
            dialogStage.close();
        }
        dialogStage.close();
    }

    private Optional<ButtonType> showAlert(String message, String details) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.setContentText(details);

        return alert.showAndWait();
    }
}
