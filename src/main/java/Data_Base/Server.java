package Data_Base;
import Config.Config;
import GUI.Alert.AlertShow;

import java.sql.*;

public class  Server {
    private Connection connection;
    private boolean conSuccessful = false;

    private Server () {
        try {
            Class.forName(Config.NAME_BIBLE);
            connection = DriverManager.getConnection(Config.URL_DB, Config.LOGIN_DB, Config.PASSWORD_DB);
            conSuccessful = true;
            System.out.println("Data Base connected");
        } catch (SQLException e){
            System.out.println("Data Base not connected \n Error!");
            System.out.println(e);
            AlertShow.showAlert("error", "Data Base not connected", "Error! \n Please check VPN or Internet connection");
        } catch (ClassNotFoundException e){
            System.out.println("JDBS not founded!");
            System.out.println(e);
            System.exit(0);
        }
    }
    private static class SignServer{
        public static final Server SIGNSERVER = new Server();
    }

    public static Server getInstance(){
        return SignServer.SIGNSERVER;
    }

    public ResultSet request(String sql){
        try {
             return connection.createStatement().executeQuery(sql);
        } catch (SQLException e){
            System.out.println("Request not correct or not connected DB");
            System.out.println(e);
        }
        return null;
    }

    public PreparedStatement cRequest(String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement;
        } catch (SQLException e){
            System.out.println("Request not correct or not connected DB");
            System.out.println(e);
        }
        return null;
    }
    private void reConnect(int tries){
        if (tries == 0){
            System.exit(0);
        }
        try {
            System.out.println(1);
            connection = DriverManager.getConnection(Config.URL_DB, Config.LOGIN_DB, Config.PASSWORD_DB);
            conSuccessful = true;
        } catch (SQLException e) {
            reConnect(--tries);
        }

    }
    public ResultSet request(PreparedStatement statement){
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            if (conSuccessful) {
                reConnect(3);
                return request(statement);
            } else {
                System.out.println(e);
                throw new RuntimeException();
            }

        }
    }
}
