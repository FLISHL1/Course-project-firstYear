package GUI;

import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("H");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }
}
