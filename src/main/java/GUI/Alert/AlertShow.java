package GUI.Alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertShow {
    public static void showAlert(String type, String title, String message, Stage stage){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /*Popup popup = new Popup();
//                popup.setAutoHide(true); // Автоматически скрывать при щелчке вне окна
                popup.setAutoFix(true);


                VBox popupBox = new VBox();
                popupBox.setStyle("-fx-background-color: white; -fx-padding: 20;");

                Label popupContent = new Label(attention);

                Button btn = new Button("OK");
                btn.setOnAction(event -> popup.hide());

                popupBox.getChildren().addAll(popupContent, btn);
                popup.getContent().add(popupBox);
                popup.show(stage);*/
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
}
