package controllers;

import crypto.PasswordHashing;
import data_Base.query.BuilderQuery;
import data_Base.query.Query;
import data_Base.Server;
import data_Base.tables.RowTabel;
import data_Base.tables.Table;
import data_Base.tables.Tables;
import gUI.ChangeUserP;
import gUI.alert.AlertShow;
import gUI.MainP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;


public class AuthController {
    @FXML
    private Button butLogin;
    @FXML
    private TextField insLogin;
    @FXML
    private TextField insPassword;

    public AuthController(){}

    @FXML
    private void buttonClick(ActionEvent event) throws SQLException {
        switch (((Button) event.getSource()).getId()){
            case "butLogin":{
                Table table;
//                          Проверка на наличие запрашиваемой таблицы в памяти
                if (!Tables.contain("User")) {
                    BuilderQuery query = new BuilderQuery("getUser", Query.GET_USER);
                    if (!insLogin.getText().equals(""))
                        query.setWhere(insLogin.getText());
                    else{
                        AlertShow.showAlert("info", "User not found","Currently user not found\nPlease check login or password", (Stage) butLogin.getScene().getWindow());
                        return;
                    }
                    ResultSet result = query.toQuery();
                    table = new Table(result);
                } else {
                    table = Tables.get("User");
                }

                RowTabel row = table.getRow("login", insLogin.getText());

//                          Авторизация
                if (row == null){
                    AlertShow.showAlert("info", "User not found","Currently user not found\nPlease check login or password", (Stage) butLogin.getScene().getWindow());
                } else if (PasswordHashing.checkPass(insPassword.getText(), (String) row.getCell(table.getColumn("password")).getValue())) {
                    if (table.get(0).get(table.getColumn("name")).toString().contains("ректор"))
                        new MainP("директор");
                    else if (table.get(0).get(table.getColumn("name")).toString().contains("Админ"))
                        new MainP("админ");
                    else if (table.get(0).get(table.getColumn("name")).toString().contains("Курьер"))
                        new MainP("курьер");
                    else if (table.get(0).get(table.getColumn("name")).toString().contains("Пользователь"))
                        new MainP("пользователь");
                    Tables.add("User", table);
                    Stage stage = (Stage) butLogin.getScene().getWindow();
                    stage.close();
                } else {
                    AlertShow.showAlert("info", "User not found","Currently user not found\nPlease check login or password", (Stage) butLogin.getScene().getWindow());

                }


                break;
            }
            case "butCancel":{
                System.exit(0);
                break;
            }
            case "btnReg":{
                new ChangeUserP(new RowTabel(), "create", "User");
            }
        }
    }
}
