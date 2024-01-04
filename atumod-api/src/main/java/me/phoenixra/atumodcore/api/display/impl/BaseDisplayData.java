package me.phoenixra.atumodcore.api.display.impl;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.data.DisplayData;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumodcore.api.placeholders.types.injectable.StaticPlaceholder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseDisplayData implements DisplayData {
    private DisplayRenderer displayRenderer;
    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final Map<String, InjectablePlaceholder> placeholders = new ConcurrentHashMap<>();
    public BaseDisplayData(DisplayRenderer displayRenderer) {
        this.displayRenderer = displayRenderer;
    }
    @Override
    public void setData(String id, String value) {
        data.put(id,value);
        InjectablePlaceholder placeholder = new StaticPlaceholder(
                "data_"+id,
                () -> value
        );
        displayRenderer.injectPlaceholders(placeholder);
        placeholders.put(id, placeholder);
    }

    @Override
    public String getData(String id) {
        return data.get(id);
    }

    @Override
    public Map<String, String> getAllData() {
        return new HashMap<>(data);
    }

    @Override
    public void removeData(String id) {
        data.remove(id);
        InjectablePlaceholder placeholder = placeholders.get(id);
        if(placeholder == null) return;
        displayRenderer.removeInjectablePlaceholder(
                placeholder
        );
        placeholders.remove(id);
    }

    @Override
    public void clearData() {
        data.clear();
        for(InjectablePlaceholder placeholder : placeholders.values()){
            displayRenderer.removeInjectablePlaceholder(
                    placeholder
            );
        }
        placeholders.clear();
    }
}
