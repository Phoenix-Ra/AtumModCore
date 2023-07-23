package me.phoenixra.atumodcore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostLoadingHandler {
    private static Map<String, List<Runnable>> events = new HashMap<>();

    protected static void runPostLoadingEvents() {
        for (Map.Entry<String, List<Runnable>> m : events.entrySet()) {
            System.out.println("[AtumModCore] Running PostLoadingEvents for mod: " + m.getKey());

            for (Runnable r : m.getValue()) {
                r.run();
            }

            System.out.println("[AtumModCore] PostLoadingEvents completed for mod: " + m.getKey());
        }
    }

    protected static void addEvent(String modid, Runnable event) {
        if (!events.containsKey(modid)) {
            events.put(modid, new ArrayList<Runnable>());
        }
        events.get(modid).add(event);
    }

}
