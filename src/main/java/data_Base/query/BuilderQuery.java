package data_Base.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data_Base.Server;
import data_Base.tables.Cell;

public class BuilderQuery {
    private HashMap<String, Cell> args;
    private String id;
    private String query;
    private int index;
    private String nameTable;
    private String where;
    private boolean whereInt;
    public BuilderQuery(String id, String query, String nameTable){
        whereInt = false;
        args = new HashMap<>();
        index = 1;
        this.id = id;
        this.query = query;
        this.nameTable = nameTable;
        where = "";
    }
    public BuilderQuery(String id, String query){
        whereInt = false;
        args = new HashMap<>();
        index = 1;
        this.id = id;
        this.query = query;
        where = "";
    }
    public BuilderQuery addArgs(String nameCol, Cell value){
        args.put(nameCol, value);
        return this;
    }

    public BuilderQuery setWhere(String where){
        this.where = where;
        return this;
    }
    public BuilderQuery setWhere(Integer where){
        whereInt = true;
        this.where = Integer.toString(where);
        return this;
    }
    public ResultSet toQuery() {
        Server server = Server.getInstance();

        formatQuery();
        PreparedStatement statement = server.cRequest(query);

        fillArgs(statement);
        fillWhere(statement);
        return server.request(statement);
    }

    public void toUpdate(){
        Server server = Server.getInstance();
        formatQuery();
        PreparedStatement statement = server.cRequest(query);

        fillArgs(statement);
        fillWhere(statement);
        server.requestUpdate(statement);

    }
    private void formatQuery(){
        StringBuilder str = new StringBuilder();
        if (!args.isEmpty()) {
            for (String key : args.keySet()) {
                str.append('`').append(key).append('`').append('=').append('?').append(',').append(' ');
            }
            str.deleteCharAt(str.length() - 1);
            str.deleteCharAt(str.length() - 1);
        }
        if (nameTable != null)
            query = String.format(query, nameTable, str);
        else
            query = String.format(query, str);
    }

    private void fillArgs(PreparedStatement statement){
        try {
            for (String key : args.keySet()) {
                if (args.get(key).getValue() instanceof Integer) {
                    statement.setInt(index++, (Integer) args.get(key).getValue());
                } else if (args.get(key).getValue() instanceof String) {
                    statement.setString(index++, args.get(key).toString());
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

//    Заполняет поле where если имеется
    private void fillWhere(PreparedStatement statement){
        try {
            if (!where.equals("")) {
                if (whereInt)
                    statement.setInt(index++, Integer.parseInt(where));
                else
                    statement.setString(index++, where);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public int getLengthArg(){
        return args.size();
    }
    public String getId() {
        return id;
    }
}
