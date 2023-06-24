module Main.Main
{
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;
    requires javafx.fxml;
    requires javafx.controls;
    requires jbcrypt;
    requires javafx.swing;

    opens gUI to javafx.fxml;
    exports gUI;
    exports controllers;
    opens controllers to javafx.fxml;
    exports data_Base;
    opens data_Base to javafx.fxml;
    exports gUI.alert;
    opens gUI.alert to javafx.fxml;
    exports data_Base.query;
    opens data_Base.query to javafx.fxml;
}

