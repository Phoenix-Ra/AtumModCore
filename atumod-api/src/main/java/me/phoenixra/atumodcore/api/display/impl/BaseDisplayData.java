package me.phoenixra.atumodcore.api.display.impl;

import me.phoenixra.atumconfig.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumconfig.api.placeholders.types.injectable.StaticPlaceholder;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.data.DisplayData;
import me.phoenixra.atumodcore.api.events.data.DisplayDataChangedEvent;
import me.phoenixra.atumodcore.api.events.data.DisplayDataRemovedEvent;
import me.phoenixra.atumconfig.api.tuples.PairRecord;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseDisplayData implements DisplayData {
    private DisplayRenderer displayRenderer;
    private final Map<String, String> data = new ConcurrentHashMap<>();
    private final Map<String, List<PairRecord<String,Long>>> dataTemp = new ConcurrentHashMap<>();

    private final Map<String, String> defaultData = new ConcurrentHashMap<>();

    private final Map<String, InjectablePlaceholder> placeholders = new ConcurrentHashMap<>();
    public BaseDisplayData(DisplayRenderer displayRenderer) {
        this.displayRenderer = displayRenderer;
        MinecraftForge.EVENT_BUS.register(this);
    }
    @Override
    public void setData(@NotNull String id, @NotNull String value) {
        //remove previous if exists to not cause conflicts
        removeDataCache(id,true);

        data.put(id,value);
        onDataChanged(id,value, DisplayDataChangedEvent.ChangeType.SET);
    }

    @Override
    public void setTemporaryData(@NotNull String id, @NotNull String value, long lifetime, boolean queued) {

        List<PairRecord<String,Long>> list = queued ? dataTemp.get(id) : null;
        if(list==null){
            list = new ArrayList<>();
        }
        boolean newValue = !queued || list.isEmpty();
        list.add(new PairRecord<>(value,

                newValue ? lifetime+System.currentTimeMillis() : lifetime)
        );

        if(newValue){
            //remove previous if exists to not cause conflicts
            removeDataCache(id, !queued);
            onDataChanged(id, value, DisplayDataChangedEvent.ChangeType.SET_TEMPORARY);
        }
        dataTemp.put(id,list);
    }

    @Override
    public void setDefaultData(@NotNull String id, @NotNull String value) {
        if(value == null)
            defaultData.remove(id);
        else
            defaultData.put(id,value);
    }

    private void onDataChanged(String id, String value, DisplayDataChangedEvent.ChangeType changeType){
        InjectablePlaceholder placeholder = new StaticPlaceholder(
                "data_"+id,
                () -> value
        );
        displayRenderer.injectPlaceholders(false, placeholder);
        placeholders.put(id, placeholder);
        MinecraftForge.EVENT_BUS.post(new DisplayDataChangedEvent(displayRenderer,id,value,changeType));
    }

    @Override
    public @Nullable String getData(@NotNull String id) {
        String out = data.get(id);
        if(out == null && dataTemp.containsKey(id)) {
            out = dataTemp.get(id).get(0).getFirst();
        }
        return out;
    }

    @Override
    public boolean hasData(@NotNull String id) {
        String out = data.get(id);
        if(out == null && dataTemp.containsKey(id)) {
            out = dataTemp.get(id).get(0).getFirst();
        }
        return out != null;
    }

    @Override
    public @NotNull Map<String, String> getAllData() {
        //merged data and dataTemp
        Map<String, String> mergedData = new HashMap<>(data);
        for(Map.Entry<String,List<PairRecord<String,Long>>> entry
                : dataTemp.entrySet()){

            mergedData.put(entry.getKey(),entry.getValue().get(0).getFirst());
        }
        return mergedData;
    }

    @Override
    public void removeData(@NotNull String id) {
        removeDataCache(id,true);
        MinecraftForge.EVENT_BUS.post(new DisplayDataRemovedEvent(displayRenderer,id));
    }
    private void removeDataCache(String id, boolean ignoreTempQueue) {
        data.remove(id);
        if(ignoreTempQueue) {
            dataTemp.remove(id);
        }else{
            //queued temp
            List<PairRecord<String,Long>> list = dataTemp.get(id);
            if(list!=null && list.size()>1) {
                List<PairRecord<String,Long>> newList = list.subList(1, list.size());
                newList.get(0).setSecond(
                        newList.get(0).getSecond()+System.currentTimeMillis()
                );
                dataTemp.put(id, newList);
            }else{
                dataTemp.remove(id);
            }
        }
        InjectablePlaceholder placeholder = placeholders.get(id);
        if(placeholder == null) return;
        displayRenderer.removeInjectablePlaceholder(false,
                placeholder
        );
        placeholders.remove(id);
    }


    @Override
    public void clearData() {
        data.clear();
        dataTemp.clear();
        defaultData.clear();
        for(InjectablePlaceholder placeholder : placeholders.values()){
            displayRenderer.removeInjectablePlaceholder(false,
                    placeholder
            );
        }
        placeholders.clear();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            long currentTime = System.currentTimeMillis();
            for(Map.Entry<String,List<PairRecord<String,Long>>> entry : dataTemp.entrySet()){
                if(entry.getValue().get(0).getSecond() < currentTime){
                    //queued temp
                    if(entry.getValue().size() > 1){
                        removeDataCache(entry.getKey(),false);
                        onDataChanged(entry.getKey(),entry.getValue().get(1).getFirst(),
                                DisplayDataChangedEvent.ChangeType.SET_TEMPORARY);
                        return;
                    }


                    if(defaultData.containsKey(entry.getKey())) {

                        removeDataCache(entry.getKey(),true);

                        data.put(entry.getKey(), defaultData.get(entry.getKey()));
                        onDataChanged(entry.getKey(), defaultData.get(entry.getKey()),
                                DisplayDataChangedEvent.ChangeType.SET_DEFAULT_BACK);

                    }else
                        removeData(entry.getKey());
                }
            }
        }
    }
}
