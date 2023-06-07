package Data_Base;
import java.sql.*;
public abstract class  Server {
    private Connection connection;
    private Statement statement;
    private String login;
    private String password;
    private String url;

    private Server (String url, String login, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.url = url;
            this.login = login;
            this.password = password;
            connection = DriverManager.getConnection(this.url, this.login, this.password);
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

    public ResultSet request(String sql){
        try {
             return connection.createStatement().executeQuery(sql);
        } catch (SQLException e){
            System.out.println("Request not correct or not connected DB");
            System.out.println(e);
        }
        return null;
    }
}
