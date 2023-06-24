package controllers;

import crypto.PasswordHashing;
import data_Base.query.BuilderQuery;
import data_Base.query.Query;
import data_Base.Server;
import data_Base.tables.RowTabel;
import data_Base.tables.Table;
import data_Base.tables.Tables;
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
    private Button butCancel;
    @FXML
    private TextField insLogin;
    @FXML
    private TextField insPassword;

    private Server server;

    public AuthController(Server server){
        this.server = server;
    }

    @FXML
    private void buttonClick(ActionEvent event) throws SQLException {
        switch (((Button) event.getSource()).getId()){
            case "butLogin":{
//                insLogin.setText("kirill.kk");
//                insPassword.setText("15022005");
                Table table;

//                          Проверка на наличие запрашиваемой таблицы в памяти
                if (!Tables.contain("User")) {
//                        PreparedStatement s = server.cRequest(Query.GET_USER);
//                        s.setString(1, insLogin.getText());
//                        ResultSet result = server.request(s);
                    BuilderQuery query = new BuilderQuery("getUser", Query.GET_USER);
                    query.setWhere(String.format("login = \"%s\"", insLogin.getText()));
                    ResultSet result = server.request(query.toString());
                    table = new Table(result);
                    System.out.println(table);
                } else {
                    table = Tables.get("User");
                }

                RowTabel row = table.getRow("login", insLogin.getText());

//                          Авторизация
                if (row == null){
                    AlertShow.showAlert("info", "User not found","Currently user not found\nPlease check login or password", (Stage) butLogin.getScene().getWindow());
                } else if (PasswordHashing.checkPass(insPassword.getText(), (String) row.getCell(table.getColumn("password")).getValue())) {
                    new MainP(server);
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
            }
        }
    }
}
