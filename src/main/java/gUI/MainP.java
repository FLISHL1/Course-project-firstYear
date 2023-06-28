package gUI;

import controllers.DirectorController;
import controllers.UserController;
import data_Base.Server;
import data_Base.query.AcumQuery;
import data_Base.tables.Tables;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainP extends Application {
    String role;
    public MainP(String role){
        this.role = role;
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
        FXMLLoader loader;
        if (role.equals("директор")) {
            loader = new FXMLLoader(getClass().getResource("rolesP/DirectPane.fxml"));
            loader.setControllerFactory(param -> new DirectorController(role));
        }
        else if (role.equals("админ")){
            loader = new FXMLLoader(getClass().getResource("rolesP/DirectPane.fxml"));
            loader.setControllerFactory(param -> new DirectorController(role));
        } else {
            loader = new FXMLLoader(getClass().getResource("rolesP/UserPane.fxml"));
            loader.setControllerFactory(param -> new UserController(role));
        }

        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Main");
        primaryStage.setResizable(false);
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Tables.clear();
                for(String id: AcumQuery.getAllName()){
                    Server.getInstance().requestUpdate(AcumQuery.get(id).toString());
                }
                AcumQuery.clear();
            }
        });
        primaryStage.show();
    }
}
