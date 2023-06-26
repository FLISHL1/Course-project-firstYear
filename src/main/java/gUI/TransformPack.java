package gUI;


import data_Base.tables.RowTabel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransformPack extends Application {
    RowTabel row;
    String type;
    public TransformPack(RowTabel row, String type){
        new JFXPanel();
        this.row = row;
        this.type = type;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rolesP/TransformPack.fxml"));
        loader.setControllerFactory(param -> new controllers.TransformPackController(row, type));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Pack");
        primaryStage.setResizable(false);
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
