package Data_Base.Query;

import java.util.HashMap;

public class AcumQuery {
    private static HashMap<String, BuilderQuery> acum = new HashMap<>();

    public static void add(BuilderQuery query){
        acum.put(query.getId(), query);
    }
    public static BuilderQuery get(String id){
        return acum.get(id);
    }
    public static boolean contain(String id){
        return acum.containsKey(id);
    }
}
