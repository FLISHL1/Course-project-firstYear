package Controllers;

import Crypto.PasswordHashing;
import Data_Base.Querys;
import Data_Base.Server;
import Data_Base.Tables.RowTabel;
import Data_Base.Tables.Table;
import Data_Base.Tables.Tables;
import GUI.Alert.AlertShow;
import GUI.MainP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
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
                insLogin.setText("kirill.kk");
                insPassword.setText("15022005");
                try{
                    Table table;

//                          Проверка на наличие запрашиваемой таблицы в памяти
                    if (!Tables.contain("User")) {
                        PreparedStatement s = server.cRequest(Querys.GET_USER);
                        s.setString(1, insLogin.getText());
                        ResultSet result = server.request(s);
                        table = new Table(result);
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
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }





                break;
            }
            case "butCancel":{
                System.exit(0);
            }
        }
    }
}
