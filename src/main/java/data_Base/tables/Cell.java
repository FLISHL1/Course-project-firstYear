package data_Base.tables;

public class Cell<T> {
    private T value;
    public Cell(T value){
        this.value = value;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = (T) value;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(value).toString();
    }
}
