package me.phoenixra.atumodcore.api.display.font;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class FontData {
    /**
     * All the character data information (regular).
     */
    protected Map<Character, DisplayFont.CharacterData> regularData = Collections.synchronizedMap(new HashMap<>());

    /**
     * All the character data information (bold).
     */
    protected Map<Character, DisplayFont.CharacterData> boldData = Collections.synchronizedMap(new HashMap<>());

    /**
     * All the character data information (italics).
     */
    protected Map<Character, DisplayFont.CharacterData> italicsData = Collections.synchronizedMap(new HashMap<>());

    protected AtomicBoolean regularDataState = new AtomicBoolean(false);
    protected AtomicBoolean boldDataState = new AtomicBoolean(false);
    protected AtomicBoolean italicDataState = new AtomicBoolean(false);
}
