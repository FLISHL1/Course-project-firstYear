package Controllers;

import Data_Base.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AuthController {
    @FXML
    private Button butLogin;
    @FXML
    private Button butCancel;


    @FXML
    private void buttonClick(ActionEvent event){
        switch (((Button) event.getSource()).getId()){
            case "butLogin":{

                Stage stage = (Stage) butLogin.getScene().getWindow();
                stage.close();
                System.out.println(1);
            }
            case "butCancel":{
                System.exit(0);
            }
        }
    }
}
