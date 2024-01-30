package me.phoenixra.atumodcore.api.events.input;

import lombok.Getter;
import me.phoenixra.atumodcore.api.input.InputType;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when an input is pressed.
 */
@Getter
public class InputPressEvent extends Event {

    private final InputType type;
    private final int mouseX;
    private final int mouseY;
    private final int mouseDeltaX;
    private final int mouseDeltaY;
    private final int mouseScrollDelta;
    private final int keyboardKey;
    private final char keyboardCharacter;

    public InputPressEvent(InputType type, int keyboardKey, char keyboardCharacter,int mouseX, int mouseY, int mouseDeltaX, int mouseDeltaY, int mouseScrollDelta) {
        if(type == InputType.KEYBOARD_KEY){
            switch (keyboardKey){
                case 29:
                    this.type = InputType.KEYBOARD_LEFT_CTRL;
                    break;
                case 157:
                    this.type = InputType.KEYBOARD_RIGHT_CTRL;
                    break;
                case 42:
                    this.type = InputType.KEYBOARD_SHIFT;
                    break;
                case 56:
                    this.type = InputType.KEYBOARD_LEFT_ALT;
                    break;
                case 184:
                    this.type = InputType.KEYBOARD_RIGHT_ALT;
                    break;
                case 28:
                    this.type = InputType.KEYBOARD_ENTER;
                    break;
                case 57:
                    this.type = InputType.KEYBOARD_SPACE;
                    break;
                case 14:
                    this.type = InputType.KEYBOARD_BACKSPACE;
                    break;
                case 15:
                    this.type = InputType.KEYBOARD_TAB;
                    break;
                case 1:
                    this.type = InputType.KEYBOARD_ESCAPE;
                    break;
                default:
                    this.type = type;
                    break;
            }
        }else{
            this.type = type;
        }
        this.keyboardKey = keyboardKey;
        this.keyboardCharacter = keyboardCharacter;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseDeltaX = mouseDeltaX;
        this.mouseDeltaY = mouseDeltaY;
        this.mouseScrollDelta = mouseScrollDelta;
    }
    public InputPressEvent(InputType type, int mouseX, int mouseY, int mouseDeltaX, int mouseDeltaY, int mouseScrollDelta) {
        this(type, 0, ' ', mouseX, mouseY, mouseDeltaX, mouseDeltaY, mouseScrollDelta);
    }
    public InputPressEvent(InputType type, int keyboardKey, char keyboardCharacter) {
        this(type, keyboardKey, keyboardCharacter, 0, 0, 0, 0, 0);
    }
}
