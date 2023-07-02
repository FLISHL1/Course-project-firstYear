package controllers;

import crypto.PasswordHashing;
import data_Base.Server;
import data_Base.query.AcumQuery;
import data_Base.query.BuilderQuery;
import data_Base.query.Query;
import data_Base.tables.Cell;
import data_Base.tables.RowTabel;
import data_Base.tables.Table;
import data_Base.tables.Tables;
import gUI.AuthP;
import gUI.TransformPack;
import gUI.alert.AlertShow;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
//    TabUser
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
//    TabPacks
    @FXML
    private TextField searchPack;
    @FXML
    private VBox listPack;
    private String role;
    private boolean fl;
    public UserController(String role) {
        this.role = role;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetChange();
        BuilderQuery query = new BuilderQuery("getUsers", Query.GET_USERS);
        Tables.add("Users", query.toQuery());

        query = new BuilderQuery("getDelCenter", Query.GET_TABLE, "Delivery_Center");
        Tables.add("Delivery_Center", query.toQuery());


        query = new BuilderQuery("getPacks", Query.GET_TABLE, "Packs");
        Tables.add("Packs", query.toQuery());
        fillPacks();
    }
    @FXML
    private void saveChange(ActionEvent event){
        Node o = (Node) event.getSource();
        for (;!(o instanceof AnchorPane); o = o.getParent());
        userSaveChange((AnchorPane) o);

    }
    @FXML
    private void createPack(){
        new TransformPack(new RowTabel(), "create");
        fillPacks();
    }
    private void fillPacks(){
        listPack.getChildren().clear();
        Table table = Tables.get("Packs");
        HBox boxColumn = new HBox(new Label("ID"), new Label("Тип доставки"), new Label("Вес"), new Label("От кого"), new Label("Кому"));
        boxColumn.setSpacing(60);
        boxColumn.setStyle("-fx-font-size: 18px; -fx-font-family: \"Lato Semibold\"; -fx-border-color: gray;");
        boxColumn.setPadding(new Insets(10));
        listPack.getChildren().add(boxColumn);
        for (RowTabel row: table) {
            boolean cont = true;
            if (role.contains("пользователь") && ((Integer) Tables.get("User").get(0).get(0).getValue()).equals((Integer) row.getCell(table.getColumn("user_from")).getValue())) {
                cont = false;
            }
            if (role.contains("пользователь") && ((Integer) Tables.get("User").get(0).get(0).getValue()).equals((Integer) row.getCell(table.getColumn("user_to")).getValue())) {
                cont = false;
            }
            if (role.contains("курьер") && ((Integer) Tables.get("User").get(0).get(0).getValue()).equals((Integer) row.getCell(table.getColumn("id_courier")).getValue())) {
                cont = false;
            }
            if(cont) continue;
            HBox box = new HBox();
            Label id = new Label((row.getCell(table.getColumn("id")).toString()));
            Label typeDeli = new Label(row.getCell(table.getColumn("type_delivery")).toString());
            Label weight = new Label(row.getCell(table.getColumn("weight")).toString());
            RowTabel user = Tables.get("Users").getRow("id", (Integer) row.getCell(table.getColumn("user_from")).getValue());
            Label userFrom = new Label(user.get(Tables.get("Users").getColumn("login")).toString());
            user = Tables.get("Users").getRow("id", (Integer) row.getCell(table.getColumn("user_to")).getValue());
            Label userTo = new Label(user.get(Tables.get("Users").getColumn("login")).toString());
            box.setSpacing(80);
            box.setStyle("-fx-font-size: 18px; -fx-font-family: \"Lato Semibold\"; -fx-border-color: gray;");
            box.setPadding(new Insets(10));
            box.getChildren().addAll(id, typeDeli, weight, userFrom, userTo);
            listPack.getChildren().add(box);
        }
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
            query = new BuilderQuery("updateUser", Query.UPDATE_USER);
        else
            query = AcumQuery.get("updateUser");
        query.setWhere(row.getCell(table.getColumn("login")).toString());

        if (!insOldPassword.getText().equals("") && PasswordHashing.checkPass(insOldPassword.getText(), row.getCell(table.getColumn("password")).toString())) {
            row.getCell(table.getColumn("password")).setValue(PasswordHashing.HashPassword(insNewPassword.getText()));
            query.addArgs("password", row.getCell(table.getColumn("password")));
        } else {
            AlertShow.showAlert("warning", "Password not corrected", "You entered the password incorrectly");
            return;
        }
        checkUser(row, "first_name", query, insFirstName);
        checkUser(row, "last_name", query, insLastName);
        checkUser(row, "second_name", query, insSecondName);
        checkUser(row, "number_phone", query, insNumberPhone);
        checkUser(row, "address", query, insAddress);

        if (query.getLengthArg() != 0)
//            AcumQuery.add(query);
            query.toUpdate();
        resetChange();
        AlertShow.showAlert("info", "Changes save", "Insertable changes, is saved", (Stage) insFirstName.getScene().getWindow());

    }
    private void checkUser(RowTabel editRow, String nameColumn, BuilderQuery query, TextField text){
        if (checkChange(editRow.getCell(Tables.get("User").getColumn(nameColumn)), text.getText())) {
            editRow.getCell(Tables.get("User").getColumn(nameColumn)).setValue(text.getText());
            query.addArgs(nameColumn, editRow.getCell(Tables.get("User").getColumn(nameColumn)));
        }
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
        Table table = Tables.get("User");
        RowTabel row = table.get(0);
        insFirstName.setText(row.get(table.getColumn("first_name")).toString());
        insSecondName.setText(row.get(table.getColumn("second_name")).toString());
        insLastName.setText(row.get(table.getColumn("last_name")).toString());
        insNumberPhone.setText(row.get(table.getColumn("number_phone")).toString());
        insAddress.setText(row.get(table.getColumn("address")).toString());
        insLogin.setText(row.get(table.getColumn("login")).toString());
        nameUser.setText(insLastName.getText() + " " + insFirstName.getText() + " " + insSecondName.getText());
        roleUser.setText(row.get(table.getColumn("name")).toString());
    }
    @FXML
    private void disRed(KeyEvent event){
        if (((TextField) event.getSource()).getStyle().contains("-fx-border-color: red"))
            ((TextField) event.getSource()).setStyle("");
    }
    @FXML
    private void exit(){
        new AuthP();
        ((Stage) insFirstName.getScene().getWindow()).close();
        Tables.clear();
        for(String id: AcumQuery.getAllName()){
            AcumQuery.get(id).toUpdate();
        }
        AcumQuery.clear();
    }
}
