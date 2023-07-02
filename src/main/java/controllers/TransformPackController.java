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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TransformPackController  implements Initializable {
    @FXML
    private ChoiceBox choiceTypeDeli;
    @FXML
    private ChoiceBox choiceTo;
    @FXML
    private ChoiceBox choiceFrom;
    @FXML
    private ChoiceBox choiceDel;
    @FXML
    private ChoiceBox choiceCourier;
    @FXML
    private TextField insWeight;
    @FXML
    private Button btnReset;

    private RowTabel row;
    private String type;
    public TransformPackController(RowTabel row, String type){
        this.row = row;
        this.type = type;
    }

    @FXML
    private void resetChange(){
        Table table = Tables.get("Packs");
        choiceTypeDeli.setValue(row.get(table.getColumn("type_delivery")).toString());
        insWeight.setText(row.get(table.getColumn("weight")).toString());

        choiceFrom.setValue(buildUserName("user_from"));
        choiceTo.setValue(buildUserName("user_to"));
        choiceDel.setValue(Tables.get("Delivery_Center").getRow("id", (Integer) row.get(table.getColumn("id_dc_to")).getValue()).toString());
        choiceCourier.setValue(buildUserName("id_courier"));

    }
    private String buildUserName(String search){
        Table table = Tables.get("Packs");
        String tableName = "Users";
        StringBuilder str = new StringBuilder();
        RowTabel rowUser = Tables.get(tableName).getRow("id", (Integer) row.get(table.getColumn(search)).getValue());
        str.append(rowUser.get(Tables.get(tableName).getColumn("login")).toString()).append(" ");
        str.append(rowUser.get(Tables.get(tableName).getColumn("last_name")).toString()).append(" ");
        str.append(rowUser.get(Tables.get(tableName).getColumn("first_name")).toString()).append(" ");
        str.append(rowUser.get(Tables.get(tableName).getColumn("second_name")).toString()).append(" ");
        str.append(rowUser.get(Tables.get(tableName).getColumn("address")).toString()).append(" ");
        return str.toString();
    }
    @FXML
    private void disRed(KeyEvent event){
        if (((TextField) event.getSource()).getStyle().contains("-fx-border-color: red"))
            ((TextField) event.getSource()).setStyle("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillChoiceBoxUsers(new ChoiceBox[]{choiceFrom, choiceTo}, "Users");
        fillChoiceBox(choiceDel, "Delivery_Center");
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Обычная", "Срочная");
        choiceTypeDeli.setItems(list);
        if (!type.equals("create")) {
            resetChange();

        } else {
            btnReset.setVisible(false);
        }
    }

    private void fillChoiceBox(ChoiceBox box, String tableName){
        ObservableList<String> list = FXCollections.observableArrayList();
        for (RowTabel row: Tables.get(tableName)){
            list.add(row.toString());
        }
        box.setItems(list);
    }
    private void fillChoiceBoxUsers(ChoiceBox[] boxs, String tableName){
        ObservableList<String> list = FXCollections.observableArrayList();
        for (RowTabel row: Tables.get(tableName)){
            if (row.get(Tables.get(tableName).getColumn("name")).toString().contains("Пользо")) {
                StringBuilder str = new StringBuilder();
                str.append(row.get(Tables.get(tableName).getColumn("login")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("last_name")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("first_name")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("second_name")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("address")).toString()).append(" ");
                list.add(str.toString());
            }
        }
        for (ChoiceBox box: boxs)
            box.setItems(list);
    }
    private void fillCourier(Integer idDC){
        ObservableList<String> list = FXCollections.observableArrayList();
        String tableName = "Users";
        for (RowTabel row: Tables.get(tableName)){

            if (((Integer) row.get(Tables.get("Users").getColumn("id_dc")).getValue()).equals(idDC)) {
                StringBuilder str = new StringBuilder();
                str.append(row.get(Tables.get(tableName).getColumn("login")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("last_name")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("first_name")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("second_name")).toString()).append(" ");
                str.append(row.get(Tables.get(tableName).getColumn("address")).toString()).append(" ");
                list.add(str.toString());
            }
        }
        choiceCourier.setItems(list);
    }

    public void showCourier(ActionEvent event) {
        if (!choiceCourier.isVisible()){
            choiceCourier.setVisible(true);
        }
        if (choiceDel.getValue() != null) {
            fillCourier(Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0]));

        }
    }
    private boolean checkChange(Cell oldValue, String newValue){
        return !oldValue.getValue().toString().equals(newValue);
    }
    private boolean checkChange(Cell oldValue, Integer newValue){
        return !oldValue.getValue().toString().equals(newValue);
    }

    public void saveChange(ActionEvent event) {
        System.out.println(row);
        if (choiceTo.getValue().equals(choiceFrom.getValue())){
            AlertShow.showAlert("warning", "Warning", "From whom and to whom can not be equal", (Stage) choiceTo.getScene().getWindow());
            return;
        }
        if (!insWeight.getText().matches("[0-9]+")){
            insWeight.setStyle("-fx-border-color: red;");
            return;
        }
        if (type.equals("create")){
            if (Tables.get("Packs").size() != 0)
                row.addInt((Integer) (Tables.get("Packs").get(Tables.get("Packs").size()-1).get(0).getValue()) + 1);
            else
                row.addInt(1);
        }
        String nameTable = "Packs";
        BuilderQuery query;
        if (type.equals("create")) {
            query = new BuilderQuery("insertPack" + row.get(0).toString(), Query.INSERT, nameTable);
        } else {
            query = new BuilderQuery("insertPack" + row.get(0).toString(), Query.UPDATE_PACK);
            query.setWhere((Integer) row.get(0).getValue());
        }
        String nameColumn = "type_delivery";
        if (choiceTypeDeli.getValue() != null){
            if (type.equals("create")) {
                row.add(new Cell<String>((String) choiceTypeDeli.getValue()));
                query.addArgs(nameColumn, row.get(Tables.get(nameTable).getColumn(nameColumn)));
            }
            else {
                if (checkChange(row.get(Tables.get(nameTable).getColumn("type_delivery")), (String) choiceTypeDeli.getValue()))
                {
                    row.get(Tables.get(nameTable).getColumn("type_delivery")).setValue((String) choiceTypeDeli.getValue());
                    query.addArgs(nameColumn, row.get(Tables.get(nameTable).getColumn(nameColumn)));
                }
            }

        }
        insertDBQ(insWeight, row, query);
        insertDBQ(choiceFrom, row, "user_from",query);
        insertDBQ(choiceTo, row, "user_to",query);
        nameColumn = "id_dc_to";
        if (choiceDel.getValue() != null) {
            if (type.equals("create")) {
                row.add(new Cell<Integer>((Integer) Tables.get("Delivery_Center").getRow("id", Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0])).get(0).getValue()));
                query.addArgs(nameColumn, row.get(Tables.get("Packs").getColumn(nameColumn)));
            }
            else {
                if (checkChange(row.get(Tables.get("Packs").getColumn(nameColumn)), (Integer) Tables.get("Delivery_Center").getRow("id", Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0])).get(0).getValue()))
                {
                    row.get(Tables.get("Packs").getColumn(nameColumn)).setValue((Integer) Tables.get("Delivery_Center").getRow("id", Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0])).get(0).getValue());
                    query.addArgs(nameColumn, row.get(Tables.get("Packs").getColumn(nameColumn)));
                }
            }
        } else {
            choiceDel.setStyle("-fx-border-color: red;");
        }


        insertDBQ(choiceCourier, row, "id_courier", query);

        query.toUpdate();
//        AcumQuery.add(query);
        if (type.equals("create"))
            Tables.get("Packs").add(row);

        ((Stage) insWeight.getScene().getWindow()).close();
    }
    private void insertDBQ(TextField text, RowTabel newRow, BuilderQuery query){
        if (type.equals("edit") && checkChange(newRow.get(Tables.get("Packs").getColumn("weight")), text.getText())) {
            newRow.get(Tables.get("Packs").getColumn("weight")).setValue(text.getText());
        } else {
            newRow.add(new Cell<String>(text.getText()));
        }
        query.addArgs("weight", newRow.get(Tables.get("Packs").getColumn("weight")));
    }
    private void insertDBQ(ChoiceBox box, RowTabel newRow, String column, BuilderQuery query){
        if (box.getValue() != null) {

            if (type.equals("create"))
                newRow.add(new Cell<Integer>((Integer) Tables.get("Users").getRow("login", ((String) box.getValue()).split(" ")[0]).get(0).getValue()));
            else {
                if (checkChange(newRow.get(Tables.get("Packs").getColumn(column)), (Integer) Tables.get("Users").getRow("login", ((String) box.getValue()).split(" ")[0]).get(0).getValue()))
                    newRow.get(Tables.get("Packs").getColumn(column)).setValue((Integer) Tables.get("Users").getRow("login", ((String) box.getValue()).split(" ")[0]).get(0).getValue());
            }
            query.addArgs(column, row.get(Tables.get("Packs").getColumn(column)));

        } else {
            box.setStyle("-fx-border-color: red;");
        }
    }
}
