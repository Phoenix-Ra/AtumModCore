package me.phoenixra.atumodcore.core.config.typehandlers;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.core.config.AtumConfigSection;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public abstract class ConfigTypeHandler {

    private static HashMap<ConfigType, ConfigTypeHandler> handlers = new HashMap<>();
    static {
        handlers.put(ConfigType.JSON, new TypeHandlerJson());
        handlers.put(ConfigType.YAML, new TypeHandlerYaml());
    }
    private ConfigType type;
    public ConfigTypeHandler(ConfigType type) {
        this.type = type;
    }
    public Map<String,Object> toMap(AtumMod atumMod, String input) {
        if (input == null || input.replace(" ", "").isEmpty()){
            return new HashMap<>();
        }

        return normalizeToConfig(atumMod, type,parseToMap(input));
    }
    protected abstract Map<String,Object> parseToMap(String input);
    public abstract String toString(Map<String,Object> map);


    public static Map<String,Object> toMap(AtumMod atumMod,@NotNull ConfigType type, @NotNull String input) {
        return handlers.get(type).toMap(atumMod,input);
    }
    public static String toString(@NotNull ConfigType type, @NotNull Map<String,Object> map) {
        return handlers.get(type).toString(map);
    }
    public static String toString(InputStream inputStream){
        try {
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static Object constrainConfigTypes(AtumMod atumMod, @NotNull ConfigType type, Object input) {
        if(input instanceof Map){
            return new AtumConfigSection(atumMod,type,normalizeToConfig(atumMod,type, (Map<?,?>) input));
        }else if(input instanceof Iterable){
            Iterator<?> iterator = ((Iterable<?>) input).iterator();
            if(!iterator.hasNext()) return new ArrayList<>();
            Object first = iterator.next();
            if(first == null){
                return new ArrayList<>();
            }
            else if(first instanceof Map){
                Iterable<Map<Object,Object>> iterable = (Iterable<Map<Object,Object>>) input;
                List<AtumConfigSection> building = new ArrayList<>();
                for(Map<Object,Object> map : iterable){
                    building.add(new AtumConfigSection(atumMod, type,normalizeToConfig(atumMod,type, map)));
                }
                return building;
            }
            else {
                List<Object> building = new ArrayList<>();
                for(Object obj : (Iterable<?>) input){
                    building.add(obj);
                }
                return building;
            }
        }
        return input;
    }

    public static Map<String,Object> normalizeToConfig(AtumMod atumMod, @NotNull ConfigType type, @NotNull Map<?,?> map) {
        Map<String,Object> building = new HashMap<>();
        for(Map.Entry<?,?> entry : map.entrySet()){
            if(entry.getKey() == null || entry.getValue() == null){
                continue;
            }
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            value = constrainConfigTypes(atumMod,type, value);
            building.put(key, value);
        }
        return building;
    }
    public static String readToString(@NotNull Reader input) {

        try(BufferedReader reader = input instanceof BufferedReader ? (BufferedReader)input : new BufferedReader(input)) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            return builder.toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }
    /*
    fun Reader.readToString(): String {
        val input = this as? BufferedReader ?: BufferedReader(this)
        val builder = StringBuilder()

        var line: String?
                input.use {
            while (it.readLine().also { read -> line = read } != null) {
                builder.append(line)
                builder.append('\n')
            }
        }

        return builder.toString()
    }*/
}
