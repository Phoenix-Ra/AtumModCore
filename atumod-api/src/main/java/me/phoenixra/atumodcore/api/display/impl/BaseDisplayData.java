package me.phoenixra.atumodcore.api.display.impl;

import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.data.DisplayData;
import me.phoenixra.atumodcore.api.events.data.DisplayDataChangedEvent;
import me.phoenixra.atumodcore.api.events.data.DisplayDataRemovedEvent;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumodcore.api.placeholders.types.injectable.StaticPlaceholder;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseDisplayData implements DisplayData {
    private DisplayRenderer displayRenderer;
    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final Map<String, PairRecord<String,Long>> dataTemp = new ConcurrentHashMap<>();

    private final Map<String, String> defaultData = new ConcurrentHashMap<>();

    private final Map<String, InjectablePlaceholder> placeholders = new ConcurrentHashMap<>();
    public BaseDisplayData(DisplayRenderer displayRenderer) {
        this.displayRenderer = displayRenderer;
        MinecraftForge.EVENT_BUS.register(this);
    }
    @Override
    public void setData(String id, String value) {
        //remove previous if exists to not cause conflicts
        removeDataCache(id);

        data.put(id,value);
        onDataChanged(id,value);
    }

    @Override
    public void setTemporaryData(String id, String value, long lifetime) {
        //remove previous if exists to not cause conflicts
        removeDataCache(id);

        dataTemp.put(id,new PairRecord<>(value, System.currentTimeMillis()+lifetime));
        onDataChanged(id,value);
    }

    @Override
    public void setDefaultData(String id, String value) {
        if(value == null)
            defaultData.remove(id);
        else
            defaultData.put(id,value);
    }

    private void onDataChanged(String id, String value){
        InjectablePlaceholder placeholder = new StaticPlaceholder(
                "data_"+id,
                () -> value
        );
        displayRenderer.injectPlaceholders(placeholder);
        placeholders.put(id, placeholder);
        MinecraftForge.EVENT_BUS.post(new DisplayDataChangedEvent(displayRenderer,id,value));
    }

    @Override
    public String getData(String id) {
        String out = data.get(id);
        if(out == null && dataTemp.containsKey(id)) {
            out = dataTemp.get(id).getFirst();
        }
        return out;
    }

    @Override
    public Map<String, String> getAllData() {
        //merged data and dataTemp
        Map<String, String> mergedData = new HashMap<>(data);
        for(Map.Entry<String,PairRecord<String,Long>> entry : dataTemp.entrySet()){
            mergedData.put(entry.getKey(),entry.getValue().getFirst());
        }
        return mergedData;
    }

    @Override
    public void removeData(String id) {
        removeDataCache(id);
        MinecraftForge.EVENT_BUS.post(new DisplayDataRemovedEvent(displayRenderer,id));
    }
    private void removeDataCache(String id) {
        data.remove(id);
        dataTemp.remove(id);
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
        dataTemp.clear();
        for(InjectablePlaceholder placeholder : placeholders.values()){
            displayRenderer.removeInjectablePlaceholder(
                    placeholder
            );
        }
        placeholders.clear();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            long currentTime = System.currentTimeMillis();
            for(Map.Entry<String,PairRecord<String,Long>> entry : dataTemp.entrySet()){
                if(entry.getValue().getSecond() < currentTime){
                    if(defaultData.containsKey(entry.getKey()))
                        setData(entry.getKey(),
                                defaultData.get(entry.getKey())
                        );
                    else
                        removeData(entry.getKey());
                }
            }
        }
    }
}
