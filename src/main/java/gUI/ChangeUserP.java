package gUI;

import data_Base.tables.RowTabel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChangeUserP extends Application {
    RowTabel row;
    String type;
    String role;
    public ChangeUserP(RowTabel row, String type, String role){
        new JFXPanel();
        this.row = row;
        this.type = type;
        this.role = role;
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
    public ChangeUserP(String type, String role){
        new JFXPanel();
        this.row = row;
        this.type = type;
        this.role = role;
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
    public ChangeUserP(RowTabel row, String type){
        new JFXPanel();
        this.row = row;
        this.type = type;
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
        FXMLLoader loader;
        if (type.equals("change")) {
            loader = new FXMLLoader(getClass().getResource("rolesP/ChangeUser.fxml"));
            loader.setControllerFactory(param -> new controllers.ChangeUserController(row, role));
            primaryStage.setTitle("ChangeUser");
        } else {
            loader = new FXMLLoader(getClass().getResource("rolesP/RegUser.fxml"));
            loader.setControllerFactory(param -> new controllers.RegUserController(role));
            primaryStage.setTitle("CreateUser");
        }
        Scene scene = new Scene(loader.load());
        primaryStage.setResizable(false);
//        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
}
