package controllers;

import crypto.PasswordHashing;
import data_Base.query.AcumQuery;
import data_Base.query.BuilderQuery;
import data_Base.query.Query;
import data_Base.Server;
import data_Base.tables.Cell;
import data_Base.tables.RowTabel;
import data_Base.tables.Table;
import data_Base.tables.Tables;
import gUI.ChangeUserP;
import gUI.alert.AlertShow;
import gUI.AuthP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private ChoiceBox<Object> newChoidceDel;
    @FXML
    private TextField insNewLogin;
    @FXML
    private TextField insNewFirstName;
    @FXML
    private TextField insNewLastName;
    @FXML
    private TextField insNewSecondName;
    @FXML
    private TextField insNewNumberPhone;
    @FXML
    private TextField insNewAddress;
    @FXML
    private TextField insRegNewPassword;
//    TabUsers
    @FXML
    private VBox listUsers;
    @FXML
    private TextField searchLogin;
//    @FXML
//    private TableView userTable;
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

        query = new BuilderQuery("getDelCenter", Query.GET_TABLE, "Delivery_Center");
        Tables.add("Delivery_Center", server.request(query.toString()));
        table = Tables.get("Delivery_Center");
        list = FXCollections.observableArrayList();
        for (RowTabel row: table){
            list.add(row.toString());
        }
        newChoidceDel.setItems(list);
        newChoidceDel.setVisible(false);
//        ObservableList<RowTabel> list1 = FXCollections.observableArrayList();
//        list1.addAll(table);
//        userTable.setItems(list1);
//        TableColumn<RowTabel, Cell> loginColumn = new TableColumn<>("Логин");
//        loginColumn.setCellValueFactory(new PropertyValueFactory<RowTabel, Cell>(""));
//        userTable.getColumns().add(loginColumn);
        fillUsers();
    }
    private void fillUsers(){
        listUsers.getChildren().clear();
        Table table = Tables.get("Users");
        HBox boxColumn = new HBox(new Label("Логин"), new Label("ФИО"), new Label("Роль"));
        boxColumn.setSpacing(150);
        boxColumn.setStyle("-fx-font-size: 18px; -fx-font-family: \"Lato Semibold\"; -fx-border-color: gray;");
        boxColumn.setPadding(new Insets(10));
        listUsers.getChildren().add(boxColumn);
        for (RowTabel row: table){
            if (((String) (row.getCell(table.getColumn("login")).getValue())).equals((String) Tables.get("User").get(0).get(Tables.get("User").getColumn("login")).getValue())){
                continue;
            }
            HBox box = new HBox();
            Label login = new Label((String) (row.getCell(table.getColumn("login")).getValue()));
            Label fio = new Label((row.getCell(table.getColumn("last_name")).getValue()) + " " + (row.getCell(table.getColumn("first_name")).getValue()) + " " + (row.getCell(table.getColumn("second_name")).getValue()));
            Label role = new Label((String) (row.getCell(table.getColumn("name")).getValue()));
            box.setSpacing(15);
            box.setStyle("-fx-font-size: 18px; -fx-font-family: \"Lato Semibold\"; -fx-border-color: gray;");
            box.setPadding(new Insets(10));
            box.getChildren().addAll(login, fio, role);
            listUsers.getChildren().add(box);
        }
    }
    @FXML
    private void saveChange(ActionEvent event){
        Node o = (Node) event.getSource();
        for (;!(o instanceof AnchorPane); o = o.getParent());
        switch (o.getId()){
            case "tabUser" -> userSaveChange((AnchorPane) o);
            case "tabRegUser" -> regUser((AnchorPane) o);
            case "tabUsers" -> changeUser((AnchorPane) o);
        }
    }

    private void changeUser(AnchorPane o) {
        RowTabel row = Tables.get("Users").getRow("login", searchLogin.getText());
        System.out.println(Tables.get("Users").getRow("login", "Ice_One"));
        System.out.println(searchLogin.getText());
        if (row == null){
            AlertShow.showAlert("info", "Not Found", "Currently user not found\n Please check valid login!", (Stage) searchLogin.getScene().getWindow());
            return;
        }
        new ChangeUserP(o, row);
    }

    @FXML
    private void delUser(){
        Tables.get("Users").delRow("login",searchLogin.getText());
        fillUsers();
        BuilderQuery query = new BuilderQuery("delUser"+searchLogin.getText(), Query.DEL_ROW, "Users");
        query.setWhere(String.format("login = \"%s\"", searchLogin.getText()));
        System.out.println(query);
        AcumQuery.add(query);
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
        if (newChoidceDel.isVisible()){
            if (newChoidceDel.getValue() == null){
                newChoidceDel.setStyle("-fx-border-color: red;");
                return;
            } else {
                newChoidceDel.setStyle("-fx-border-color: none;");
            }
        }
        if (newChoidceBox.getValue() == null){
            newChoidceBox.setStyle("-fx-border-color: red;");
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

        RowTabel newRow = new RowTabel();

        newRow.addInt((Integer) (Tables.get("Users").get(Tables.get("Users").size()-1).get(0).getValue()) + 1);

        Tables.get("Users").add(newRow);
        int i = 1;
        newRow.add(new Cell<String>(insNewFirstName.getText()));
        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));

        newRow.add(new Cell<String>(insNewLastName.getText()));
        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));

        newRow.add(new Cell<String>(insNewSecondName.getText()));
        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));

        newRow.add(new Cell<String>(insNewNumberPhone.getText()));
        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));

        newRow.add(new Cell<String>(insNewAddress.getText()));
        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));

        if (newChoidceDel.isVisible()) {
            newRow.add(new Cell<Integer>(Integer.parseInt(((String) newChoidceDel.getValue()).split(" ")[0])));
            query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));
        } else
        {
            newRow.add(new Cell<Integer>(0));
            i++;
        }

        newRow.add(new Cell<String>(insNewLogin.getText()));
        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));

        newRow.add(new Cell<String>(PasswordHashing.HashPassword(insRegNewPassword.getText())));

        query.addArgs(Tables.get("Users").getColumn(i), newRow.get(i++));
        newRow.add(new Cell<String>(((String) newChoidceBox.getValue()).split(" ")[1]));
        BuilderQuery query1 = new BuilderQuery("InsertRole", Query.INSERT_ROLE);
        query1.addArgs("", new Cell<Integer>(Integer.parseInt(((String) newChoidceBox.getValue()).split(" ")[0])));
        query1.setWhere(String.format("login = \"%s\"",insNewLogin.getText()));


        AcumQuery.add(query);
        AcumQuery.add(query1);
        System.out.println(Tables.get("Users"));

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
        resetChange();
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
    private void showDelCenter(ActionEvent event){

        if(!((String) newChoidceBox.getValue()).contains("Курьер")){
            newChoidceDel.setVisible(false);
        } else {
            newChoidceDel.setVisible(true);
        }
    }

    @FXML
    private void exit(){
        new AuthP(server);
        ((Stage) insFirstName.getScene().getWindow()).close();
        Tables.clear();
        for(String id: AcumQuery.getAllName()){
            System.out.println(AcumQuery.get(id).toString());
            server.requestUpdate(AcumQuery.get(id).toString());
        }
    }
}
