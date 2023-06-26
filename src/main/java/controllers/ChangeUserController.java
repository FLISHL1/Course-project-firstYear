package controllers;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeUserController implements Initializable {
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
    private ChoiceBox choiceDel;
    @FXML
    private ChoiceBox choiceRole;
    private boolean fl;
    private RowTabel row;
    public ChangeUserController(RowTabel row){
        this.row = row;
    }
    @FXML
    private void saveChange(ActionEvent event){
        Node source = (Node) event.getSource();
        for (;!(source instanceof AnchorPane); source = source.getParent());
        ObservableList<Node> list = ((AnchorPane) source).getChildren();
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
        BuilderQuery query;
        if (!AcumQuery.contain("updateUser"+row.get(table.getColumn("login"))))
            query = new BuilderQuery("updateUser", Query.UPDATE, "Users");
        else
            query = AcumQuery.get("updateUser"+row.get(table.getColumn("login")));
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
        if (checkChange(row.getCell(table.getColumn("name")), ((String) choiceRole.getValue()).split(" ")[1])){
            row.getCell(table.getColumn("name")).setValue(((String) choiceRole.getValue()).split(" ")[1]);
            BuilderQuery query1 = new BuilderQuery("changeRole" + insLogin.getText(), Query.UPDATE, "Role_User");
            query1.addArgs("id_role", new Cell<Integer>(Integer.parseInt(((String) choiceRole.getValue()).split(" ")[0])));
            query1.setWhere("id_user = " + row.get(0).getValue());
            AcumQuery.add(query1);
        }
        if (!row.get(Tables.get("Users").getColumn("login")).toString().equals(insLogin.getText())) {
            if (Tables.get("Users").getRow("login", insLogin.getText()) == null) {
                row.getCell(table.getColumn("login")).setValue(insLogin.getText());
                query.addArgs("login", row.getCell(table.getColumn("login")));
            } else {
                AlertShow.showAlert("warning", "Login is available", "Login is available");
                return;
            }
        }
        if (choiceDel.isVisible() && checkChange(row.getCell(table.getColumn("id_dc")), Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0]))){
            row.getCell(table.getColumn("id_dc")).setValue(Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0]));
            query.addArgs("id_dc", row.getCell(table.getColumn("id_dc")));
        }

        if (query.getLengthArg() != 0)
            AcumQuery.add(query);
        ((Stage) insLogin.getScene().getWindow()).close();
    }
    private boolean checkChange(Cell oldValue, String newValue){
        return !oldValue.toString().equals(newValue);
    }
    private boolean checkChange(Cell oldValue, Integer newValue){
        return !oldValue.getValue().equals(newValue);
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
    private void showDelCenter(ActionEvent event){
        if(choiceRole.getValue() != null && !((String) choiceRole.getValue()).contains("Курьер")){
            choiceDel.setVisible(false);
        } else {
            choiceDel.setVisible(true);
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
                    Table table = Tables.get("Users");
                    insFirstName.setText(row.get(table.getColumn("first_name")).toString());
                    insSecondName.setText(row.get(table.getColumn("second_name")).toString());
                    insLastName.setText(row.get(table.getColumn("last_name")).toString());
                    insNumberPhone.setText(row.get(table.getColumn("number_phone")).toString());
                    insAddress.setText(row.get(table.getColumn("address")).toString());
                    insLogin.setText(row.get(table.getColumn("login")).toString());
                    choiceRole.setValue(Tables.get("Roles").getRow("name", row.get(table.getColumn("name")).toString()).toString());
                    if (((Integer) row.get(table.getColumn("id_dc")).getValue()) != 0)
                        choiceDel.setValue(Tables.get("Delivery_Center").getRow("id", (Integer) row.get(table.getColumn("id_dc")).getValue()).toString());

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Table table = Tables.get("Roles");
        ObservableList<Object> list = FXCollections.observableArrayList();
        for (RowTabel row: table){
            if (!((String) row.getCell(1).getValue()).contains("Директ"))
                list.add(row.toString());
        }
        choiceRole.setItems(list);
        table = Tables.get("Delivery_Center");
        list = FXCollections.observableArrayList();
        for (RowTabel row: table){
            list.add(row.toString());
        }
        choiceDel.setItems(list);
        choiceDel.setVisible(false);
        resetChange();

    }
}
