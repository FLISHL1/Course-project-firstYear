import Data_Base.Server;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Server server = new Server("jdbc:mysql://std-mysql.ist.mospolytech.ru:3306/std_2228_kursach",
                "std_2228_kursach", "15022005");
        System.out.println(server.request("SELECT * FROM Roles"));
    }
}
