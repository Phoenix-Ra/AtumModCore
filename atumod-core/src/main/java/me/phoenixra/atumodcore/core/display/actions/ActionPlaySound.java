package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Action that opens the singleplayer menu.
 * <br>
 * Usage Example: 'play_sound@{sound_resource};{volume};{pitch}'
 */
@RegisterDisplayAction(templateId = "play_sound")
public class ActionPlaySound implements DisplayAction {
    @Override
    public void perform(ActionData actionData) {
        if(actionData.getActionArgs() == null ||
                actionData.getActionArgs().getArgs().length < 1)
            return;
        int argsLength = actionData.getActionArgs().getArgs().length;
        String soundId = actionData.getActionArgs().getArgs()[0];
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(
                new net.minecraft.util.ResourceLocation(soundId)
        );
        if(soundEvent == null){
            return;
        }
        float volume = argsLength > 1 ? Float.parseFloat(actionData.getActionArgs().getArgs()[1]) : 1.0f;
        float pitch = argsLength > 2 ? Float.parseFloat(actionData.getActionArgs().getArgs()[2]) : 1.0f;

        Minecraft.getMinecraft().player.playSound(soundEvent,
                volume, pitch);

    }
}
