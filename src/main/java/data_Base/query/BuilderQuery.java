package data_Base.query;

import java.util.HashMap;
import data_Base.tables.Cell;

public class BuilderQuery {
    private HashMap<String, Cell> args = new HashMap<>() {
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
    };
    private String id;
    private String query;
    private String where;
    private String nameTable;
    public BuilderQuery(String id, String query, String nameTable){
        this.id = id;
        this.query = query;
        this.nameTable = nameTable;
        where = "";
    }
    public BuilderQuery(String id, String query){
        this.id = id;
        this.query = query;
        where = "";
    }
    public BuilderQuery addArgs(String nameCol, Cell value){
        args.put(nameCol, value);
        return this;
    }

    public BuilderQuery setWhere(String where){
        this.where = "WHERE ";
        this.where += where;
        return this;
    }
    @Override
    public String toString() {
        if (!args.isEmpty() && nameTable != null)
            return String.format(query, nameTable, args.toString(), where);
        if (nameTable != null)
            return String.format(query, nameTable, where);
        else if (!args.isEmpty())
            return String.format(query, args.toString(), where);
        else
            return String.format(query, where);
    }

    public int getLengthArg(){
        return args.size();
    }
    public String getId() {
        return id;
    }
}
