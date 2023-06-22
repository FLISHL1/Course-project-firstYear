package Controllers;

import Crypto.PasswordHashing;
import Data_Base.Query.AcumQuery;
import Data_Base.Query.BuilderQuery;
import Data_Base.Query.Query;
import Data_Base.Server;
import Data_Base.Tables.Cell;
import Data_Base.Tables.RowTabel;
import Data_Base.Tables.Table;
import Data_Base.Tables.Tables;
import GUI.Alert.AlertShow;
import GUI.AuthP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField insFirstName;
    @FXML
    private TextField insSecondName;
    @FXML
    private TextField insLastName;
    @FXML
    private TextField insNumberPhone;
    @FXML
    private TextField insAddress;
    @FXML
    private TextField insLogin;
    @FXML
    private TextField insOldPassword;
    @FXML
    private TextField insNewPassword;
    @FXML
    private Label nameUser;
    @FXML
    private Label roleUser;
//    TabRegUser
    @FXML
    private ChoiceBox<Object> newChoidceBox;
    @FXML
    private TextField insNewLogin;
    private Server server;
    private boolean fl;
    public MainController(Server server) {
        this.server = server;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetChange();
        BuilderQuery query = new BuilderQuery("getUsers", Query.GET_USER);
        Tables.add("Users", server.request(query.toString()));
        query = new BuilderQuery("getRole", Query.GET_TABLE, "Roles");
        Tables.add("Roles", server.request(query.toString()));
        Table table = Tables.get("Roles");
        ObservableList<Object> list = FXCollections.observableArrayList();
        for (RowTabel row: table){
            if (!((String) row.getCell(1).getValue()).contains("Директ"))
                list.add(row.toString());
        }
        newChoidceBox.setItems(list);
    }

    @FXML
    private void saveChange(ActionEvent event){
        Node o = (Node) event.getSource();
        for (;!(o instanceof AnchorPane); o = o.getParent());
        switch (o.getId()){
            case "tabUser" -> userSaveChange((AnchorPane) o);
            case "tabRegUser" -> regUser((AnchorPane) o);
        }
    }
    private void regUser(AnchorPane source){
        ObservableList<Node> list = source.getChildren();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i) instanceof ScrollPane && ((ScrollPane) list.get(i)).getContent() instanceof VBox) {
                list = ((VBox) ((ScrollPane) list.get(i)).getContent()).getChildren();
                break;
            }
        }
        checkValid(list);

        if (fl){
            AlertShow.showAlert("warning", "Warning", "Invalid input", (Stage) insFirstName.getScene().getWindow());
            return;
        }

        Table table = Tables.get("Users");
        RowTabel row = table.getRow("login", insNewLogin.getText());
        if (row != null){
            AlertShow.showAlert("warning", "Warning", "This login already exists", (Stage) insFirstName.getScene().getWindow());
            return;
        }

        BuilderQuery query;
        if (!AcumQuery.contain("RegUser"))
            query = new BuilderQuery("RegUser", Query.INSERT, "Users");
        else
            query = AcumQuery.get("RegUser");

        System.out.println(newChoidceBox.getValue());

    }


    private void userSaveChange(AnchorPane source){
        ObservableList<Node> list = source.getChildren();
        for (int i = 0; i < list.size(); i++){
            if (list.get(i) instanceof ScrollPane && ((ScrollPane) list.get(i)).getContent() instanceof VBox) {
                list = ((VBox) ((ScrollPane) list.get(i)).getContent()).getChildren();
                break;
            }
        }
        checkValid(list);

        if (fl){
            AlertShow.showAlert("warning", "Warning", "Invalid input", (Stage) insFirstName.getScene().getWindow());
            return;
        }

        Table table = Tables.get("User");
        RowTabel row = table.get(0);
        BuilderQuery query;
        if (!AcumQuery.contain("updateUser"))
            query = new BuilderQuery("updateUser", Query.UPDATE, "Users");
        else
            query = AcumQuery.get("updateUser");
        query.setWhere(String.format("login = %s", row.getCell(table.getColumn("login")).getValue()));

        if (checkChange(row.getCell(table.getColumn("first_name")), insFirstName.getText())) {
            row.getCell(table.getColumn("first_name")).setValue(insFirstName.getText());
            query.addArgs("first_name", row.getCell(table.getColumn("first_name")));
        }
        if (checkChange(row.getCell(table.getColumn("last_name")), insLastName.getText())) {
            row.getCell(table.getColumn("last_name")).setValue(insLastName.getText());
            query.addArgs("last_name", row.getCell(table.getColumn("last_name")));
        }
        if (checkChange(row.getCell(table.getColumn("second_name")), insSecondName.getText())) {
            row.getCell(table.getColumn("second_name")).setValue(insSecondName.getText());
            query.addArgs("second_name", row.getCell(table.getColumn("second_name")));
        }
        if (checkChange(row.getCell(table.getColumn("number_phone")), insNumberPhone.getText())) {
            row.getCell(table.getColumn("number_phone")).setValue(insNumberPhone.getText());
            query.addArgs("number_phone", row.getCell(table.getColumn("number_phone")));
        }

        if (checkChange(row.getCell(table.getColumn("address")), insAddress.getText())) {
            row.getCell(table.getColumn("address")).setValue(insAddress.getText());
            query.addArgs("address", row.getCell(table.getColumn("address")));
        }

        if (PasswordHashing.checkPass(insOldPassword.getText(), (String) row.getCell(table.getColumn("password")).getValue())) {
            row.getCell(table.getColumn("password")).setValue(PasswordHashing.HashPassword(insNewPassword.getText()));
            query.addArgs("password", row.getCell(table.getColumn("password")));
        }
        if (query.getLengthArg() != 0)
            AcumQuery.add(query);
    }

    private boolean checkChange(Cell oldValue, String newValue){
        return !oldValue.getValue().toString().equals(newValue);

    }

    private void checkValid(ObservableList<Node> list){
        String regName = "([A-Z][a-z]*)|([А-Я][а-я]*)";
        String regNumberPhone = "(\\+[1-9]+ \\([0-9]{3}\\) [0-9]{3} [0-9]{2}-[0-9]{2})|(8[0-9]{10})";
        String regLogin = "([A-z](.|[A-z])*)";
        String regAddress = "(.+)|(.+)";
        fl = false;
        TextField textField;
        for(Node node: list){
            if (node instanceof HBox){
                for(Node nodeChild: ((HBox) node).getChildren()){
                    if (nodeChild instanceof TextField){
                        textField = (TextField) nodeChild;
                        if (textField.getId().contains("Name"))
                            valid(textField, regName);
                        else if (textField.getId().contains("Phone"))
                            valid(textField, regNumberPhone);
                        else if (textField.getId().contains("Login"))
                            valid(textField, regLogin);
                        else if (textField.getId().contains("Address"))
                            valid(textField, regAddress);
                    }

                }
            }
        }
    }
    private void valid(TextField text, String reg){
        if(!text.getText().matches(reg)) {
            text.setStyle("-fx-border-color: red;");
            fl = true;
        }
    }
    @FXML
    private void resetChange(){
        boolean fl = false;
        for (Thread t: Thread.getAllStackTraces().keySet())
            if (t.getName().equals("Reset")) fl = true;
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
        ((Stage) insFirstName.getScene().getWindow()).close();
        Tables.clear();
    }



}
