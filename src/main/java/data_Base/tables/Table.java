package data_Base.tables;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class Table extends ArrayList<RowTabel> {
    private ArrayList<String> nameCol;

    public Table(ResultSet result){
        nameCol = new ArrayList<String>() {
            @Override
            public String toString() {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < size(); i++)
                    str.append(get(i)).append(" ");

                return str.toString();
            }
        };

        try {
            add(result);
            initName(result.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initName(ResultSetMetaData met) throws SQLException {
        for (int i = 1; i <= met.getColumnCount(); i++) {
            nameCol.add(met.getColumnName(i));
        }
    }

    private void add(ResultSet result) throws SQLException {
        while (result.next()){
            add(new RowTabel(result));
        }
    }

    public RowTabel getRow(String column, String searchValue){
        RowTabel row = null;
        for (int i = 0; i < size(); i++) {
            row = get(i).get(getColumn(column), searchValue);
            if (row != null){
                break;
            }
        }
        return row;
    }
    public RowTabel getRow(String column, Integer searchValue){
        RowTabel row = null;
        for (int i = 0; i < size(); i++) {
            row = get(i).get(getColumn(column), searchValue);
            if (row != null){
                break;
            }
        }
        return row;
    }
    public int getColumn(String column){
        return nameCol.indexOf(column);
    }
    public String getColumn(Integer column){
        return nameCol.get(column);
    }
    public RowTabel delRow(String column, String searchValue){
        RowTabel row = null;
        for (int i = 0; i < size(); i++) {
            row = get(i).get(getColumn(column), searchValue);
            if (row != null){
                break;
            }
        }
        this.remove(row);
        return row;
    }
    public RowTabel delRow(String column, Integer searchValue){
        RowTabel row = null;
        for (int i = 0; i < size(); i++) {
            row = get(i).get(getColumn(column), searchValue);
            if (row != null){
                break;
            }
        }
        this.remove(row);
        return row;
    }
    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        str.append(nameCol.toString()).append("\n");
        for (int i = 0; i < size(); i++) str.append(get(i).toString()).append("\n");

        return str.toString();
    }
}
