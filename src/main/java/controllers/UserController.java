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
    private Server server;
    private boolean fl;
    public UserController(String role) {
        server = Server.getInstance();
        this.role = role;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetChange();
        BuilderQuery query = new BuilderQuery("getUsers", Query.GET_USER);
        Tables.add("Users", server.request(query.toString()));

        query = new BuilderQuery("getDelCenter", Query.GET_TABLE, "Delivery_Center");
        Tables.add("Delivery_Center", server.request(query.toString()));


        query = new BuilderQuery("getPacks", Query.GET_TABLE, "Packs");
        Tables.add("Packs", server.request(query.toString()));
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
            query = new BuilderQuery("updateUser", Query.UPDATE, "Users");
        else
            query = AcumQuery.get("updateUser");
        query.setWhere(String.format("login = \"%s\"", row.getCell(table.getColumn("login")).getValue()));

        if (!insOldPassword.getText().equals("") && PasswordHashing.checkPass(insOldPassword.getText(), (String) row.getCell(table.getColumn("password")).getValue())) {
            row.getCell(table.getColumn("password")).setValue(PasswordHashing.HashPassword(insNewPassword.getText()));
            query.addArgs("password", row.getCell(table.getColumn("password")));
        } else {
            AlertShow.showAlert("warning", "Password not corrected", "You entered the password incorrectly");
            return;
        }
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

        if (query.getLengthArg() != 0)
            AcumQuery.add(query);
        resetChange();
        AlertShow.showAlert("info", "Changes save", "Insertable changes, is saved", (Stage) insFirstName.getScene().getWindow());

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
        insFirstName.setText((String) row.get(table.getColumn("first_name")).getValue());
        insSecondName.setText((String) row.get(table.getColumn("second_name")).getValue());
        insLastName.setText((String) row.get(table.getColumn("last_name")).getValue());
        insNumberPhone.setText((String) row.get(table.getColumn("number_phone")).getValue());
        insAddress.setText((String) row.get(table.getColumn("address")).getValue());
        insLogin.setText((String) row.get(table.getColumn("login")).getValue());
        nameUser.setText(insLastName.getText() + " " + insFirstName.getText() + " " + insSecondName.getText());
        roleUser.setText((String) row.get(table.getColumn("name")).getValue());
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
            server.requestUpdate(AcumQuery.get(id).toString());
        }
        AcumQuery.clear();
    }
}
