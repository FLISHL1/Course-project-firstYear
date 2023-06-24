package gUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import data_Base.Server;

public class MainP extends Application {
    private Server server;
    public MainP(Server server){
        this.server = server;
        new JFXPanel();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rolesP/DirectPane.fxml"));
        loader.setControllerFactory(param -> new controllers.MainController(server));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Main");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
