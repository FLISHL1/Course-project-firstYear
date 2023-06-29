package data_Base.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data_Base.Server;
import data_Base.tables.Cell;

public class BuilderQuery {
    private HashMap<String, Cell> args = new HashMap<>()/* {
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key: keySet()){
                if (stringBuilder.length() != 0)
                    stringBuilder.append(", ");
                if (!key.equals("")){
                    stringBuilder.append("`").append(key).append("`").append("=");
                }
                if (get(key).getValue() instanceof String){
                    stringBuilder.append("\"").append(get(key).getValue()).append("\"");
                } else if (get(key).getValue() instanceof Integer){
                    stringBuilder.append(get(key).getValue());
                } else {
                    stringBuilder.append("\"").append(get(key).getValue()).append("\"");
                }
            }
            return stringBuilder.toString();
        }
    }*/;
    private String id;
    private String query;
    private int index;
    private String nameTable;
    private String where;
    private boolean whereInt;
    public BuilderQuery(String id, String query, String nameTable){
        whereInt = false;
        index = 1;
        this.id = id;
        this.query = query;
        this.nameTable = nameTable;
        /*try {
            this.query.setString(index++, nameTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        where = "";
    }
    public BuilderQuery(String id, String query){
        whereInt = false;
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
    @Override
    public String toString() {
        if (!args.isEmpty() && nameTable != null){
            return String.format(query, nameTable, args.toString(), where);
        }
        if (nameTable != null)
            return String.format(query, nameTable, where);
        else if (!args.isEmpty())
            return String.format(query, args.toString(), where);
        else
            return String.format(query, where);
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
