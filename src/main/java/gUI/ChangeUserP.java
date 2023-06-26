package gUI;

import data_Base.Server;
import data_Base.tables.RowTabel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChangeUserP extends Application {
    RowTabel row;
    public ChangeUserP(RowTabel row){
        new JFXPanel();
        this.row = row;
       /* Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });*/
        try {
            start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rolesP/ChangeUser.fxml"));
        loader.setControllerFactory(param -> new controllers.ChangeUserController(row));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("ChangeUser");
        primaryStage.setResizable(false);
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
}
