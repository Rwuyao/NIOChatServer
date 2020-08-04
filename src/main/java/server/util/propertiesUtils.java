package server.util;

import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public  class propertiesUtils {

    private static final ConcurrentHashMap<String,ResourceBundle> resources= new ConcurrentHashMap<>();

    public static String getString(String path,String key){
        ResourceBundle resource;
        if(resources.containsKey(path)){
            resource= resources.get(path);
        }else{
            resource = ResourceBundle.getBundle(path);
            resources.put(path,resource);
        }
        return resource.getString(key);
    }

    public static int getInt(String path,String key){
        ResourceBundle resource;
        if(resources.containsKey(path)){
            resource= resources.get(path);
        }else{
            resource = ResourceBundle.getBundle(path);
            resources.put(path,resource);
        }
        return Integer.parseInt(resource.getString(key));
    }
}
