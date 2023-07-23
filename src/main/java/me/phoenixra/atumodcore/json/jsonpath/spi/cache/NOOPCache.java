package me.phoenixra.atumodcore.json.jsonpath.spi.cache;

import me.phoenixra.atumodcore.json.jsonpath.JsonPath;

public class NOOPCache implements Cache {

    @Override
    public JsonPath get(String key) {
        return null;
    }

    @Override
    public void put(String key, JsonPath value) {
    }
}
