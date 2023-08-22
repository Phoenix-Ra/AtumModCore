package me.phoenixra.atumodcore.api.misc.properties;

import java.util.HashMap;

public class PropertiesSection {
    private String sectionType;
    private HashMap<String, String> entries = new HashMap<>();

    public PropertiesSection(String sectionType) {
        this.sectionType = sectionType;
    }

    public void addEntry(String name, String value) {
        this.entries.putIfAbsent(name, value);
    }

    public HashMap<String, String> getEntries() {
        return this.entries;
    }

    public String getEntryValue(String name) {
        return this.entries.get(name);
    }

    public void removeEntry(String name) {
        if (this.entries.containsKey(name)) {
            this.entries.remove(name);
        }
    }

    public boolean hasEntry(String name) {
        return this.entries.containsKey(name);
    }

    public String getSectionType() {
        return this.sectionType;
    }
}
