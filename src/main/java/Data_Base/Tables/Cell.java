package Data_Base.Tables;

import java.util.HashMap;

public class Cell<T> {
    private T value;
    public Cell(T value){
        this.value = value;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
