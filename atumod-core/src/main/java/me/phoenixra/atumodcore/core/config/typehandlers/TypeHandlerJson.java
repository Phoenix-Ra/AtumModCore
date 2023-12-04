package me.phoenixra.atumodcore.core.config.typehandlers;

import com.google.gson.*;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.core.config.AtumConfigSection;

import java.lang.reflect.Type;
import java.util.Map;

public class TypeHandlerJson extends ConfigTypeHandler {
    private AtumGsonSerializer serializer = new AtumGsonSerializer();
    public TypeHandlerJson() {
        super(ConfigType.JSON);
    }

    @Override
    protected Map<String, Object> parseToMap(String input) {
        return serializer.gson.fromJson(input, Map.class);
    }

    @Override
    public String toString(Map<String, Object> map) {
        return serializer.gson.toJson(map);
    }


    private static class AtumGsonSerializer implements JsonSerializer<Config> {
        protected Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(AtumConfigSection.class, this)
                .create();


        @Override
        public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
            return gson.toJsonTree(src.toMap());
        }

        /*@Override
        public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }*/
    }
}
