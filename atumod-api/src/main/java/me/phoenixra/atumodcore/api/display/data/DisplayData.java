package me.phoenixra.atumodcore.api.display.data;

import java.util.Map;

//@TODO change it to be more lightweight for the network
//Cause it might be costly if overused once we get many players online
public interface DisplayData {


    void setData(String id, String value);

    void setTemporaryData(String id, String value, long lifetime, boolean queued);
    void setDefaultData(String id, String value);
    String getData(String id);
    default String getDataOrDefault(String id, String defaultValue){
        String data = getData(id);
        if(data==null) return defaultValue;
        return data;
    }
    default boolean isElementEnabled(String id){
        String data = getData("element_enabled$"+id);
        if(data==null) return true;
        return Boolean.parseBoolean(data);
    }
    default void setElementEnabled(String id, boolean enabled){
        setData("element_enabled$"+id, String.valueOf(enabled));
    }
    Map<String,String> getAllData();
    void removeData(String id);
    void clearData();
}
