package me.phoenixra.atumodcore.api.input.event;

import lombok.Getter;
import me.phoenixra.atumodcore.api.input.InputType;

/**
 * Called when an input is released.
 */
public class InputReleaseEvent {

    @Getter
    private final InputType type;
    @Getter
    private final int mouseX;
    @Getter
    private final int mouseY;
    @Getter
    private final int mouseDeltaX;
    @Getter
    private final int mouseDeltaY;
    @Getter
    private final int mouseScrollDelta;
    @Getter
    private final int keyboardKey;
    @Getter
    private final char keyboardCharacter;


    public InputReleaseEvent(InputType type, int keyboardKey, char keyboardCharacter,int mouseX, int mouseY, int mouseDeltaX, int mouseDeltaY, int mouseScrollDelta) {
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
    public InputReleaseEvent(InputType type, int mouseX, int mouseY, int mouseDeltaX, int mouseDeltaY, int mouseScrollDelta) {
        this(type, 0, ' ', mouseX, mouseY, mouseDeltaX, mouseDeltaY, mouseScrollDelta);
    }
    public InputReleaseEvent(InputType type, int keyboardKey, char keyboardCharacter) {
        this(type, keyboardKey, keyboardCharacter, 0, 0, 0, 0, 0);
    }
}
