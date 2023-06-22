module GUI.App
{
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;
    requires javafx.fxml;
    requires javafx.controls;
    requires jbcrypt;

    opens GUI to javafx.fxml;
    exports GUI;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Data_Base;
    opens Data_Base to javafx.fxml;
    exports GUI.Alert;
    opens GUI.Alert to javafx.fxml;
    exports Data_Base.Query;
    opens Data_Base.Query to javafx.fxml;
}

