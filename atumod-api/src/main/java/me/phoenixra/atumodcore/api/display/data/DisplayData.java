package me.phoenixra.atumodcore.api.display.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

//@TODO change it to be more lightweight for the network
//Cause it might be costly if overused once we get many players online
public interface DisplayData {


    /**
     * Set data for the display element.
     *
     * @param id    The id of the data.
     * @param value The value of the data.
     */
    void setData(@NotNull String id, @NotNull String value);

    /**
     * Set temporary data for the display element.
     * <p></p>
     * Temporary data will be removed after the lifetime.
     * <p></p>
     * If the data is queued, it will be set after the current data is removed.
     * <p>If the data is not queued, it will be set immediately.</p>
     *
     * @param id       The id of the data.
     * @param value    The value of the data.
     * @param lifetime The lifetime of the data.
     * @param queued   Whether the data should be queued.
     */
    void setTemporaryData(@NotNull String id,
                          @NotNull String value,
                          long lifetime, boolean queued);

    /**
     * Set default data for the display element.
     * <p></p>
     * Default data will be also applied when temporary data is removed.
     *
     * @param id The id of the data.
     * @param value The value of the data.
     */
    void setDefaultData(@NotNull String id, @NotNull String value);

    /**
     * Get data from the display element.
     *
     * @param id The id of the data.
     * @return The value of the data.
     */
    @Nullable String getData(@NotNull String id);

    /**
     * Check if the display element has data.
     *
     * @param id The id of the data.
     * @return Whether the display element has data.
     */
    boolean hasData(@NotNull String id);

    /**
     * Get data from the display element or the default value.
     *
     * @param id The id of the data.
     * @param defaultValue The default value.
     * @return The value of the data or the default value if not found.
     */
    @NotNull
    default String getDataOrDefault(@NotNull String id, @NotNull String defaultValue){
        String data = getData(id);
        if(data==null) return defaultValue;
        return data;
    }

    /**
     * Get if the display element is enabled.
     *
     * @param id The id of the data.
     * @return Whether the display element is enabled.
     */
    default boolean isElementEnabled(@NotNull String id){
        String data = getData("element_enabled$"+id);
        if(data==null) return true;
        return Boolean.parseBoolean(data);
    }

    /**
     * Set if the display element is enabled.
     *
     * @param id The id of the data.
     * @param enabled Whether the display element is enabled.
     */
    default void setElementEnabled(@NotNull String id, boolean enabled){
        setData("element_enabled$"+id, String.valueOf(enabled));
    }

    /*
     * Get the data map.
     *
     * @return The data map.
     */
    @NotNull Map<String,String> getAllData();

    /**
     * Remove data from the display element.
     *
     * @param id The id of the data.
     */
    void removeData(@NotNull String id);

    /**
     * Clear all data from the display element.
     */
    void clearData();
}
