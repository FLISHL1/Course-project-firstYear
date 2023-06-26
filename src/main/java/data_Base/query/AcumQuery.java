package data_Base.query;

import java.util.LinkedHashMap;
import java.util.Set;

public class AcumQuery {
    private static LinkedHashMap<String, BuilderQuery> acum = new LinkedHashMap<String, BuilderQuery>();

    public static void add(BuilderQuery query){
        acum.put(query.getId(), query);
    }
    public static BuilderQuery get(String id){
        return acum.get(id);
    }
    public static Set<String> getAllName(){
        return acum.keySet();
    }
    public static boolean contain(String id){
        return acum.containsKey(id);
    }
    public static void clear(){
        acum.clear();
    }
}
