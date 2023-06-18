package Data_Base.Tables;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class RowTabel extends ArrayList<Cell> {
    public RowTabel(ResultSet result){
        try {
            ResultSetMetaData metaData = result.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                switch (metaData.getColumnTypeName(i).toLowerCase()){
                    case "varchar" -> add(new Cell<String>(result.getString(i)));
                    case "int", "int unsigned" -> add(new Cell<Integer>(result.getInt(i)));
                    case "date" -> add(new Cell<Date>(result.getDate(i)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public RowTabel get(int indexC, String value){
        return get(indexC).getValue().equals(value)? this:null;
    }
    public Cell getCell(int indexC){
        return get(indexC);
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size(); i++) str.append(get(i).getValue()).append(" ");

        return str.toString();
    }
}
