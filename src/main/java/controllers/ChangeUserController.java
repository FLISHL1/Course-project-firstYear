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
    private String role;
    public ChangeUserController(RowTabel row, String role){
        this.row = row;
        this.role = role;
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
            query = new BuilderQuery("updateUser", Query.UPDATE_USER);
        else
            query = AcumQuery.get("updateUser"+row.get(table.getColumn("login")));
        query.setWhere(row.getCell(table.getColumn("login")).toString());

        checkUser(row, "first_name", query, insFirstName);
        checkUser(row, "last_name", query, insLastName);
        checkUser(row, "second_name", query, insSecondName);
        checkUser(row, "number_phone", query, insNumberPhone);
        checkUser(row, "address", query, insAddress);
        if (checkChange(row.getCell(table.getColumn("name")), ((String) choiceRole.getValue()).split(" ")[1])){
            row.getCell(table.getColumn("name")).setValue(((String) choiceRole.getValue()).split(" ")[1]);

            BuilderQuery query1 = new BuilderQuery("changeRole" + insLogin.getText(), Query.UPDATE_ROLE);
            query1.addArgs("id_role", new Cell<Integer>(Integer.parseInt(((String) choiceRole.getValue()).split(" ")[0])));
            query1.setWhere((Integer) row.get(0).getValue());
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
//
//        if (query.getLengthArg() != 0)
//            AcumQuery.add(query);
        if (query.getLengthArg() != 0){
            query.toUpdate();
        }
        ((Stage) insLogin.getScene().getWindow()).close();
    }
    private void checkUser(RowTabel editRow, String nameColumn, BuilderQuery query, TextField text){
        if (checkChange(editRow.getCell(Tables.get("User").getColumn(nameColumn)), text.getText())) {
            editRow.getCell(Tables.get("User").getColumn(nameColumn)).setValue(text.getText());
            query.addArgs(nameColumn, editRow.getCell(Tables.get("User").getColumn(nameColumn)));
        }
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
            if (!(row.getCell(1).toString()).contains("Директ")){
                if (role.equals("админ")){
                    if (!row.getCell(1).toString().contains("директ") && !row.getCell(1).toString().contains("Админ")){
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
        resetChange();

    }
}
