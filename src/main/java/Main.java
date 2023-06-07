import Data_Base.Server;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Server server = Server.getInstance();
        ResultSet r = server.request("SELECT * FROM Roles");
        while (r.next()){
            System.out.println(r.getInt("id") + " " + r.getString("name"));
        }
    }
}
