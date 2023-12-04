package me.phoenixra.atumodcore.core.config;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.core.config.typehandlers.ConfigTypeHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AtumConfig implements Config {
    @Getter
    private AtumMod atumMod;
    private ConfigType configType;
    private List<InjectablePlaceholder> injections;
    private ConcurrentHashMap<String, Object> values = new ConcurrentHashMap<>();

    public AtumConfig(AtumMod atumMod, ConfigType configType, Map<String, Object> values) {
        this.atumMod = atumMod;
        this.configType = configType;
        this.values.putAll(values);
    }
    public AtumConfig(AtumMod atumMod, ConfigType configType) {
         this(atumMod, configType, new ConcurrentHashMap<>());
    }
    protected void applyData(Map<String, Object> values){
        this.values.clear();
        this.values.putAll(values);
    }

    @Override
    public @NotNull List<String> getKeys(boolean deep) {
        return deep? recurseKeys(Sets.newConcurrentHashSet(),"") : new ArrayList<>(values.keySet());
    }

    @Override
    public @NotNull List<String> recurseKeys(@NotNull Set<String> current, @NotNull String root) {
        Set<String> list = Sets.newConcurrentHashSet();
        for (String key : getKeys(false)) {
            list.add(root+key);
            Object found = get(key);

            if (found instanceof Config) {
                list.addAll(((Config) found).recurseKeys(current, root+key+"."));
            }

        }
        return new ArrayList<>(list);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        String nearestPath = path.split("\\.")[0];
        if(path.contains(".")){
            String remainingPath = path.replaceFirst(nearestPath+"\\.","");
            if(remainingPath.isEmpty()){
                return null;
            }
            Object first = get(nearestPath);
            if(first instanceof Config){
                return ((Config) first).get(remainingPath);
            }
            return null;
        }
        return values.get(nearestPath);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object obj) {
        String nearestPath = path.split("\\.")[0];
        if(path.contains(".")){
            String remainingPath = path.replaceFirst(nearestPath+"\\.","");
            if(remainingPath.isEmpty()){
                return;
            }
            Config section = getSubsectionOrNull(nearestPath);
            if(section==null){
                section = new AtumConfig(getAtumMod(),configType);
            }
            section.set(remainingPath, obj);
            values.put(nearestPath, section);
            return;
        }
        if(obj == null){
            values.remove(nearestPath);
        }else{
            values.put(nearestPath, ConfigTypeHandler.constrainConfigTypes(atumMod,configType,obj));
        }
    }

    @Override
    public String toPlaintext() {
        return ConfigTypeHandler.toString(configType,this.values);
    }

    @Override
    public boolean hasPath(@NotNull String path) {
        return get(path) != null;
    }


    @Override
    public @Nullable Integer getIntOrNull(@NotNull String path) {
        Object obj = get(path);
        return (obj instanceof Number)? ((Number) obj).intValue() : null;
    }

    @Override
    public @Nullable List<Integer> getIntListOrNull(@NotNull String path) {
        List<Number> list = getList(path, Number.class);
        if(list == null){
            return null;
        }
        return list.stream().map(Number::intValue).collect(Collectors.toList());
    }
    @Override
    public @Nullable Double getDoubleOrNull(@NotNull String path) {
        Object obj = get(path);
        return (obj instanceof Number)? ((Number) obj).doubleValue() : null;
    }

    @Override
    public @Nullable List<Double> getDoubleListOrNull(@NotNull String path) {
        List<Number> list = getList(path, Number.class);
        if(list == null){
            return null;
        }
        return list.stream().map(Number::doubleValue).collect(Collectors.toList());
    }
    @Override
    public @Nullable Boolean getBoolOrNull(@NotNull String path) {
        Object obj = get(path);
        return (obj instanceof Boolean)? (Boolean) obj : null;
    }

    @Override
    public @Nullable List<Boolean> getBoolListOrNull(@NotNull String path) {
        return getList(path, Boolean.class);
    }

    @Override
    public @Nullable String getStringOrNull(@NotNull String path) {
        Object obj = get(path);
        return obj != null ? obj.toString() : null;
    }

    @Override
    public @Nullable List<String> getStringListOrNull(@NotNull String path) {
        List<Object> list = getList(path, Object.class);
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public @Nullable Config getSubsectionOrNull(@NotNull String path) {
        Object obj = get(path);
        return (obj instanceof Config)? (Config) obj : null;
    }

    @Override
    public @Nullable List<? extends Config> getSubsectionListOrNull(@NotNull String path) {
        Config section = getSubsectionOrNull(path);
        if(section == null){
            return null;
        }
        List<Config> list = new ArrayList<>();
        for(String key : section.getKeys(false)){
            Object obj = section.get(key);
            if(obj instanceof Config){
                list.add((Config) obj);
            }
        }
        return list;
    }

    @Override
    public double getEvaluated(@NotNull String path, @NotNull PlaceholderContext context) {
        String text = getStringOrNull(path);
        if(text == null){
            return 0.0;
        }
        return AtumAPI.getInstance().evaluate(getAtumMod(),text, context.withInjectableContext(this));
    }

    private<T> List<T> getList(String path, Class<T> type){

        Object obj = get(path);
        if(!(obj instanceof Iterable)){
            return null;
        }
        Iterator<?> iterator = ((Iterable<?>) obj).iterator();

        if(iterator.hasNext()){
            Object first = iterator.next();
            if(first.getClass().isAssignableFrom(type)){
                List<T> list = new ArrayList<>();
                list.add((T) first);
                while(iterator.hasNext()){
                    list.add((T) iterator.next());
                }
                return list;
            }
        }
        return new ArrayList<>();
    }
    public void addInjectablePlaceholder(List<InjectablePlaceholder> placeholders) {
        injections.addAll(placeholders);
    }
    public void removeInjectablePlaceholder(List<InjectablePlaceholder> placeholders) {
        injections.removeAll(placeholders);
    }

    @Override
    public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {

    }

    @Override
    public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {

    }

    public void clearInjectedPlaceholders() {
        injections.clear();
    }
    public List<InjectablePlaceholder> getPlaceholderInjections() {
        return injections;
    }

    @Override
    public @NotNull ConfigType getType() {
        return configType;
    }

    @Override
    public Map<String, Object> toMap() {
        return values;
    }


}
