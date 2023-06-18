package GUI;

import Data_Base.Server;
import GUI.Page.AuthP;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {
    public static void main(String[] args){
        Server server = Server.getInstance();
        AuthP a = new AuthP(server);
    }

    @Override
    public void start(Stage primaryStage){
       ;
    }
}
