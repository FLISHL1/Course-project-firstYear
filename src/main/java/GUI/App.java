package GUI;

import Data_Base.Server;
import GUI.Alert.AlertShow;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {
    public static void main(String[] args){
        Server server = Server.getInstance();
        new AuthP(server);
    }

    @Override
    public void start(Stage primaryStage){
       ;
    }
}
