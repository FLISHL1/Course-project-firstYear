package Data_Base.Tables;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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
        for (int i = 0; i < size(); i++) row = get(i).get(getColumn(column), searchValue);
        return row;
    }
    public int getColumn(String column){
        return nameCol.indexOf(column);
    }
    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        str.append(nameCol.toString()).append("\n");
        for (int i = 0; i < size(); i++) str.append(get(i).toString()).append("\n");

        return str.toString();
    }
}
