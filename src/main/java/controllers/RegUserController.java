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
import gUI.alert.AlertShow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class RegUserController implements Initializable {
    private boolean fl;
    private String role;
    @FXML
    private ChoiceBox choiceRole;
    @FXML
    private TextField insFirstName;
    @FXML
    private TextField insLastName;
    @FXML
    private TextField insSecondName;
    @FXML
    private TextField insNumberPhone;
    @FXML
    private TextField insAddress;
    @FXML
    private ChoiceBox choiceDel;
    @FXML
    private TextField insLogin;
    @FXML
    private TextField insPassword;

    public RegUserController(String role){
        this.role = role;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (role.equals("User")){
            ((VBox) choiceDel.getParent().getParent()).getChildren().removeAll(choiceDel.getParent(), choiceRole.getParent());
        } else {
            ObservableList<Object> list = FXCollections.observableArrayList();
            Table table = Tables.get("Roles");
            for (RowTabel row: table){

                if (!((String) row.getCell(1).getValue()).contains("Директ")){
                    if (role.equals("Админ")){
                        if (!((String) row.getCell(1).getValue()).contains("директ") && !((String) row.getCell(1).getValue()).contains("админ")){
                            list.add(row.toString());
                        }
                    } else {
                        list.add(row.toString());
                    }
                }
            }
            choiceRole.setItems(list);

            table = Tables.get("Delivery_Center");
            list = FXCollections.observableArrayList();
            for (RowTabel row: table){
                list.add(row.toString());
            }
            choiceDel.setItems(list);
            choiceDel.setVisible(false);
        }

        if (!Tables.contain("Users")) {
            BuilderQuery query = new BuilderQuery("getUsers", Query.GET_USER);
            Tables.add("Users", Server.getInstance().request(query.toString()));
        }

    }

    @FXML
    private void showDelCenter() {
        choiceDel.setVisible(((String) choiceRole.getValue()).contains("Курьер"));
    }

    @FXML
    private void disRed(KeyEvent event) {
        Node node = (Node) event.getSource();
        if (node.getStyle().contains("-fx-border-color: red"))
            node.setStyle("");
    }

    private void disRed(Node node){
        if (node.getStyle().contains("-fx-border-color: red"))
            node.setStyle("");
    }

    private void onRed(Node node){
        node.setStyle("-fx-border-color: red");
    }

    @FXML
    private void saveChange(ActionEvent event) {
        Node o = (Node) event.getSource();
        for (;!(o instanceof AnchorPane); o = o.getParent());

        if (!regUser((AnchorPane) o)){
            return;
        }
        if (role.equals("User")) {
            for (String id : AcumQuery.getAllName()) {
                System.out.println(AcumQuery.get(id).toString());
                Server.getInstance().requestUpdate(AcumQuery.get(id).toString());
            }
            AcumQuery.clear();
        }
        ((Stage) o.getScene().getWindow()).close();
    }
    private void insertDBQ(TextField text, RowTabel newRow, BuilderQuery query){
        newRow.add(new Cell<String>(text.getText()));
        query.addArgs(Tables.get("Users").getColumn(newRow.size()-1), newRow.get(newRow.size()-1));
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
            onRed(text);
            fl = true;
        }
    }

    private boolean regUser(AnchorPane source){
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
            return false;
        }
        if (!role.equals("User") && choiceDel.isVisible()){
            if (choiceDel.getValue() == null){
                onRed(choiceDel);
                return false;
            } else {
                disRed(choiceDel);
            }
        }
        if (!role.equals("User") && choiceRole.getValue() == null){
            onRed(choiceRole);
            return false;
        }
        RowTabel row = Tables.get("Users").getRow("login", insLogin.getText());
        if (row != null){
            AlertShow.showAlert("warning", "Warning", "This login already exists", (Stage) insFirstName.getScene().getWindow());
            return false;
        }


        BuilderQuery query;
        if (!AcumQuery.contain("RegUser"))
            query = new BuilderQuery("RegUser", Query.INSERT, "Users");
        else
            query = AcumQuery.get("RegUser");

        RowTabel newRow = new RowTabel();
        if (!role.equals("User")) {
            newRow.addInt((Integer) (Tables.get("Users").get(Tables.get("Users").size() - 1).get(0).getValue()) + 1);
            Tables.get("Users").add(newRow);
        } else {
           newRow.addInt(1);
        }

        insertDBQ(insFirstName, newRow, query);
        insertDBQ(insLastName, newRow, query);
        insertDBQ(insSecondName, newRow, query);
        insertDBQ(insNumberPhone, newRow, query);
        insertDBQ(insAddress, newRow, query);


//      Работа с выбором пункта доставки
        if (!role.equals("User") && choiceDel.isVisible()) {
            newRow.add(new Cell<Integer>(Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0])));
            query.addArgs(Tables.get("Users").getColumn(newRow.size() - 1), newRow.get(newRow.size() - 1));
        } else
        {
            newRow.add(new Cell<Integer>(0));
        }
        insertDBQ(insLogin, newRow, query);


        newRow.add(new Cell<String>(PasswordHashing.HashPassword(insPassword.getText())));
        query.addArgs(Tables.get("Users").getColumn(newRow.size() - 1), newRow.get(newRow.size() - 1));
        insPassword.clear();
        BuilderQuery query1 = new BuilderQuery("InsertRole", Query.INSERT_ROLE);
//      Работа с ролью
        if (!role.equals("User")) {
            newRow.add(new Cell<String>(((String) choiceRole.getValue()).split(" ")[1]));
            query1.addArgs("", new Cell<Integer>(Integer.parseInt(((String) choiceRole.getValue()).split(" ")[0])));
        }
        else {
            newRow.add(new Cell<String>("Пользователь"));
            query1.addArgs("", new Cell<Integer>(5));
        }
        query1.setWhere(String.format("login = \"%s\"", insLogin.getText()));
        AcumQuery.add(query);
        AcumQuery.add(query1);
        AlertShow.showAlert("info", "Successful", "User " + insLogin.getText() + " created", (Stage) insFirstName.getScene().getWindow());
        return true;
    }


}
