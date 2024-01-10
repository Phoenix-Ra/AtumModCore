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

import java.util.Arrays;

public class TriggerDataChanged extends BaseTrigger {

    private String dataId;
    private String value;

    private DisplayDataChangedEvent.ChangeType changeType;

    public TriggerDataChanged(AtumMod atumMod) {
        super(atumMod);
    }


    @Override
    public boolean filter(DisplayTriggerData triggerData) {

        if(triggerData.getArgs().length > 0 &&
        dataId != null && !dataId.equals(triggerData.getArgs()[0])){
            return false;
        }
        if(triggerData.getArgs().length > 1 &&
                value != null && !value.equals(triggerData.getArgs()[1])){
            return false;
        }
        if(triggerData.getArgs().length > 2 &&
                changeType != null && !changeType.name().equalsIgnoreCase(triggerData.getArgs()[2])){
            return false;
        }
        return true;
    }

    @SubscribeEvent
    public void onDataChanged(DisplayDataChangedEvent event){
        trigger(new DisplayTriggerData(
                event.getDataId()+";"+event.getValue()+";"+event.getChangeType().name()
                )
        );
    }


    @Override
    public DisplayTrigger cloneWithNewVariables(@NotNull Config config, @Nullable DisplayRenderer owner) {
        DisplayTrigger trigger = super.cloneWithNewVariables(config, owner);
        if(config.hasPath("filters")) {
            ((TriggerDataChanged)trigger).dataId = config.getStringOrNull("filters.data_id");
            ((TriggerDataChanged)trigger).value = config.getStringOrNull("filters.value");
            //from string
            if(config.hasPath("filters.change_type")) {
                ((TriggerDataChanged)trigger).changeType = DisplayDataChangedEvent.ChangeType.valueOf(
                        config.getString("filters.change_type").toUpperCase()
                );
            }
        }
        return trigger;
    }
}
