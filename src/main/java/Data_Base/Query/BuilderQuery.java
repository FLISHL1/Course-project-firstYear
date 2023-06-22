package Data_Base.Query;

import java.util.HashMap;
import Data_Base.Tables.Cell;

public class BuilderQuery {
    private HashMap<String, Cell> args = new HashMap<>() {
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key: keySet()){
                if (stringBuilder.length() != 0)
                    stringBuilder.append(", ");
                stringBuilder.append(key).append("=");
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
    public void addArgs(String nameCol, Cell value){
        args.put(nameCol, value);
    }

    public void setWhere(String where){
        this.where = "WHERE ";
        this.where += where;
    }
    @Override
    public String toString() {
        if (!args.isEmpty() && nameTable != null)
            return String.format(query, nameTable,args.toString(), where);
        if (nameTable != null)
            return String.format(query, nameTable, where);
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
