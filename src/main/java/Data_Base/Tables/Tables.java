package Data_Base.Tables;

import javafx.application.Platform;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Tables{
    private static HashMap<String, Table> tables = new HashMap<String, Table>();
    public static void add(ResultSet result) throws SQLException {
        tables.put(result.getMetaData().getTableName(1), new Table(result));
    }
    public static void add(String name, Table table){
        tables.put(name, table);
    }
    public static void add(String name, ResultSet result){
        tables.put(name, new Table(result));
    }

    public static Table get(String name){
        return tables.get(name);
    }

    public static Set<String> getAllName(){
        return tables.keySet();
    }

    public static boolean contain(String name){
        return tables.containsKey(name);
    }

    public static Table pop(String name){
        return tables.remove(name);
    }

}
