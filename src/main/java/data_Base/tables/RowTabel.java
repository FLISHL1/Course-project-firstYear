package data_Base.tables;

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
    public RowTabel(){
        ;
    }

    public void addInt(Integer num){
        add(new Cell<Integer>(num));
    }
    public void addString(String str){
        add(new Cell<String>(str));
    }
    public RowTabel get(int indexC, String value){
        return super.get(indexC).getValue().equals(value)? this:null;
    }
    public Cell getCell(int indexC){
        return super.get(indexC);
    }

    public void setCell(int indexC, Object value){
        super.get(indexC).setValue(value);
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size(); i++) str.append(get(i).getValue()).append(" ");

        return str.toString();
    }
}
