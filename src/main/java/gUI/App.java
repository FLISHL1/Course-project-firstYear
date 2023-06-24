package gUI;

import data_Base.Server;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
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
