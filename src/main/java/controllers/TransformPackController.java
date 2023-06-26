package controllers;

import data_Base.tables.RowTabel;
import data_Base.tables.Table;
import data_Base.tables.Tables;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

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
    RowTabel row;
    String type;
    public TransformPackController(RowTabel row, String type){
        this.row = row;
        this.type = type;
    }

    @FXML
    private void resetChange(){
        Table table = Tables.get("Packs");
/*        insFirstName.setText((String) row.get(table.getColumn("first_name")).getValue());
        insSecondName.setText((String) row.get(table.getColumn("second_name")).getValue());
        insLastName.setText((String) row.get(table.getColumn("last_name")).getValue());
        insNumberPhone.setText((String) row.get(table.getColumn("number_phone")).getValue());
        insAddress.setText((String) row.get(table.getColumn("address")).getValue());
        insLogin.setText((String) row.get(table.getColumn("login")).getValue());
        nameUser.setText(insLastName.getText() + " " + insFirstName.getText() + " " + insSecondName.getText());
        roleUser.setText((String) row.get(table.getColumn("name")).getValue());*/

    }

    @FXML
    private void disRed(KeyEvent event){
        if (((TextField) event.getSource()).getStyle().contains("-fx-border-color: red"))
            ((TextField) event.getSource()).setStyle("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillsChoiceBox(new ChoiceBox[]{choiceFrom, choiceTo}, "Users");
        fillChoiceBox(choiceDel, "Delivery_Center");
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Обычная", "Срочная");
        choiceTypeDeli.setItems(list);
        if (!type.equals("create"))
            fillCourier(Integer.parseInt(((String) choiceTypeDeli.getValue()).split(" ")[0]));
    }

    private void fillChoiceBox(ChoiceBox box, String tableName){
        ObservableList<String> list = FXCollections.observableArrayList();
        for (RowTabel row: Tables.get(tableName)){
            list.add(row.toString());
        }
        box.setItems(list);
    }
    private void fillsChoiceBox(ChoiceBox[] boxs, String tableName){
        ObservableList<String> list = FXCollections.observableArrayList();
        for (RowTabel row: Tables.get(tableName)){
            list.add(row.toString());
        }
        for (ChoiceBox box: boxs)
            box.setItems(list);
    }
    private void fillCourier(Integer idDC){
        ObservableList<String> list = FXCollections.observableArrayList();

        for (RowTabel row: Tables.get("Users")){
            if (((Integer) row.get(Tables.get("Users").getColumn("id_dc")).getValue()).equals(idDC))
                list.add(row.toString());
        }
        choiceCourier.setItems(list);
    }

    public void showCourier(ActionEvent event) {
        if (!choiceCourier.isVisible()){
            choiceCourier.setVisible(true);
        }
        System.out.println(((String) choiceDel.getValue()).split(" ")[0]);
        if (choiceDel.getValue() != null)
            fillCourier(Integer.parseInt(((String) choiceDel.getValue()).split(" ")[0]));
    }

    public void saveChange(ActionEvent event) {
    }
}
