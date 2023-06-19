package Controllers;

import Data_Base.Server;
import Data_Base.Tables.RowTabel;
import Data_Base.Tables.Table;
import Data_Base.Tables.Tables;
import GUI.Alert.AlertShow;
import GUI.AuthP;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Tab tab1;
    @FXML
    private Label firstName;
    @FXML
    private TextField insFirstName;
    @FXML
    private Label secondName;
    @FXML
    private TextField insSecondName;
    @FXML
    private Label lastName;
    @FXML
    private TextField insLastName;
    @FXML
    private Label numberPhone;
    @FXML
    private TextField insNumberPhone;
    @FXML
    private Label address;
    @FXML
    private TextField insAddress;
    @FXML
    private Label login;
    @FXML
    private TextField insLogin;
    @FXML
    private Label password;
    @FXML
    private TextField insOldPassword;
    @FXML
    private Label nameUser;
    @FXML
    private Label roleUser;
    private Server server;
    private boolean fl;
    public MainController(Server server) {
        this.server = server;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetChange();
        insFirstName.textProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    @FXML
    private void saveChange(){
        String regName = "([A-Z][a-z]*)|([А-Я][а-я]*)";
        String regNumberPhone = "(\\+[1-9]+ \\([0-9]{3}\\) [0-9]{3} [0-9]{2}-[0-9]{2})|(8[0-9]{10})";
        String regLogin = "([A-z](.|[A-z])*)";
        fl = false;
        checkVal(insFirstName, regName);
        checkVal(insSecondName, regName);
        checkVal(insLastName, regName);
        checkVal(insNumberPhone, regNumberPhone);
        checkVal(insLogin, regLogin);

        if (fl){
            AlertShow.showAlert("warning", "Warning", "Invalid input", (Stage) firstName.getScene().getWindow());
            return;
        }

    }

    private void checkVal(TextField text, String reg){
        if(!text.getText().matches(reg)) {
            text.setStyle("-fx-border-color: red;");
            fl = true;
        }
    }
    @FXML
    private void resetChange(){
        boolean fl = false;
        for (Thread t: Thread.getAllStackTraces().keySet())
            if (t.getName() == "Reset") fl = true;
        if (!fl) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Table table = Tables.get("User");
                    RowTabel row = table.get(0);
                    insFirstName.setText((String) row.get(table.getColumn("first_name")).getValue());
                    insSecondName.setText((String) row.get(table.getColumn("second_name")).getValue());
                    insLastName.setText((String) row.get(table.getColumn("last_name")).getValue());
                    insNumberPhone.setText((String) row.get(table.getColumn("number_phone")).getValue());
                    insAddress.setText((String) row.get(table.getColumn("address")).getValue());
                    insLogin.setText((String) row.get(table.getColumn("login")).getValue());
                    nameUser.setText(insLastName.getText() + " " + insFirstName.getText() + " " + insSecondName.getText());
                    roleUser.setText((String) row.get(table.getColumn("name")).getValue());
                }
            }, "Reset");
            t.start();
        }
    }

    @FXML
    private void disRed(KeyEvent event){
        if (((TextField) event.getSource()).getStyle().contains("-fx-border-color: red"))
            ((TextField) event.getSource()).setStyle("");
    }


    @FXML
    private void exit(){
        new AuthP(server);
        ((Stage) firstName.getScene().getWindow()).close();
        Tables.clear();
    }



}
