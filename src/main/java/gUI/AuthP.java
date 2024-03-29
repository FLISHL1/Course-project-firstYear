package gUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.embed.swing.JFXPanel;
import java.io.IOException;


public class AuthP extends Application{

    public AuthP() {
        new JFXPanel();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    start(new Stage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthPage.fxml"));
        loader.setControllerFactory(param -> new controllers.AuthController());
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
