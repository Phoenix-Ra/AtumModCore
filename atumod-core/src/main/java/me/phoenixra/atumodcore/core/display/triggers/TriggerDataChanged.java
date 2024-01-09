package me.phoenixra.atumodcore.core.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.impl.BaseTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerData;
import me.phoenixra.atumodcore.api.events.data.DisplayDataChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerDataChanged extends BaseTrigger {

    private String dataId;
    private String value;

    public TriggerDataChanged(AtumMod atumMod) {
        super(atumMod);
    }


    @Override
    public boolean filter(DisplayTriggerData triggerData) {
        if(triggerData.getArgs().length != 2){
            return true;
        }
        if(dataId != null && !dataId.equals(triggerData.getArgs()[0])){
            return false;
        }
        if(value != null && !value.equals(triggerData.getArgs()[1])){
            return false;
        }
        return true;
    }

    @SubscribeEvent
    public void onDataChanged(DisplayDataChangedEvent event){
        trigger(new DisplayTriggerData(event.getDataId()+";"+event.getValue()));
    }


    @Override
    public DisplayTrigger cloneWithNewVariables(@NotNull Config config, @Nullable DisplayRenderer owner) {
        DisplayTrigger trigger = super.cloneWithNewVariables(config, owner);
        if(config.hasPath("filters")){
            dataId = config.getStringOrNull("filters.data_id");
            value = config.getStringOrNull("filters.value");
        }
        return trigger;
    }
}
