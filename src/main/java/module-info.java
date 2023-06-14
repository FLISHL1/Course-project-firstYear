module GUI.App
{
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;
    requires javafx.fxml;
    requires javafx.controls;

    opens GUI to javafx.fxml;
    exports GUI;
    exports Controllers;
    opens Controllers to javafx.fxml;

}