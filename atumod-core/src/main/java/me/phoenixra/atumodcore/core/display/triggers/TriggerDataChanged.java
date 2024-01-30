package me.phoenixra.atumodcore.core.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayTrigger;
import me.phoenixra.atumodcore.api.display.impl.BaseTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTrigger;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerData;
import me.phoenixra.atumodcore.api.events.data.DisplayDataChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Triggered when the data is changed.
 * <br><br>
 * Filters:
 * <ul>
 *     <li>data_id - the data id</li>
 *     <li>value - the data value</li>
 *     <li>change_type - the change type</li>
 * </ul>
 * For change types, see {@link DisplayDataChangedEvent.ChangeType}
 */
@RegisterDisplayTrigger(templateId = "data_changed")
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

    @Override
    public @NotNull DisplayTrigger updateVariables(@NotNull Config config) {
        if(config.hasPath("filters")) {
            dataId = config.getStringOrNull("filters.data_id");
            value = config.getStringOrNull("filters.value");
            //from string
            if(config.hasPath("filters.change_type")) {
                changeType = DisplayDataChangedEvent.ChangeType.valueOf(
                        config.getString("filters.change_type").toUpperCase()
                );
            }
        }
        return this;
    }

    @Override
    public @NotNull DisplayTrigger onClone(@NotNull DisplayTrigger cloned) {
        return cloned;
    }

    @SubscribeEvent
    public void onDataChanged(DisplayDataChangedEvent event){
        trigger(new DisplayTriggerData(
                        event.getDataId()+";"+event.getValue()+";"+event.getChangeType().name()
                )
        );
    }
}
