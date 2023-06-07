package Data_Base;
import config.Config;
import java.sql.*;
public class  Server {
    private Connection connection;
    private String login;
    private String password;
    private String url;

    private Server () {
        try {
            Class.forName(Config.NAME_BIBLE);
            connection = DriverManager.getConnection(Config.URL_DB, Config.LOGIN_DB, Config.PASSWORD_DB);
            System.out.println("Data Base connected");
        } catch (SQLException e){
            System.out.println("Data Base not connected \n Error!");
            System.out.println(e);
            System.exit(0);
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

//    public ResultSet request(String sql, Object[] parameters){
//        try {
//
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setArray();
//        } catch (SQLException e){
//            System.out.println("Request not correct or not connected DB");
//            System.out.println(e);
//        }
//        return null;
//    }
}
