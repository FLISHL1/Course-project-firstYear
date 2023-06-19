package GUI.Alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertShow {
    public static void showAlert(String type, String title, String message, Stage stage){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert.AlertType typeAlert = switch (type){
                    case "info" -> Alert.AlertType.INFORMATION;
                    case "error" -> Alert.AlertType.ERROR;
                    case "warning" -> Alert.AlertType.WARNING;
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                };
                Alert alert = new Alert(typeAlert);
                alert.setTitle(title);
                alert.setContentText(message);
                alert.initOwner(stage);
                alert.show();
            }
        });
    }
    public static void showAlert(String type, String title, String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert.AlertType typeAlert = switch (type){
                    case "info" -> Alert.AlertType.INFORMATION;
                    case "error" -> Alert.AlertType.ERROR;
                    case "warning" -> Alert.AlertType.WARNING;
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                };
                Alert alert = new Alert(typeAlert);
                alert.setTitle(title);
                alert.setContentText(message);
                alert.show();
            }
        });
    }
}
