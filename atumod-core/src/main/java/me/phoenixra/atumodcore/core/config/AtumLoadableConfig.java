package me.phoenixra.atumodcore.core.config;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.utils.Objects;
import me.phoenixra.atumodcore.core.config.typehandlers.ConfigTypeHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class AtumLoadableConfig extends AtumConfig implements LoadableConfig {
    private String subDirectoryPath;
    @Getter
    private File file;
    public AtumLoadableConfig(AtumMod atumMod,
                              ConfigType type,
                              String subDirectoryPath,
                              String confName,
                              boolean forceResourceLoad) {
        super(atumMod,type);
        this.subDirectoryPath = subDirectoryPath;
        File dir = new File(atumMod.getDataFolder(), subDirectoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file = new File(dir, confName + "." + type.getFileExtension());
        if (!file.exists()) {
            createFile(forceResourceLoad);
        }else if(shouldUpdateConfig()){
            file.delete();
            createFile(forceResourceLoad);
        }
        try {
            reload();
            atumMod.getConfigManager().addConfig(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public AtumLoadableConfig(AtumMod atumMod, File file) {
        this(atumMod,ConfigType.fromFile(file),file.getPath()
                .replace(atumMod.getDataFolder().getPath(),"")
                .replace(file.getName(),""),
                file.getName().split("\\.")[0],
                false
        );
    }

    public boolean shouldUpdateConfig() {
        try(InputStream inputStream = getAtumMod().getClass().getResourceAsStream(getResourcePath())) {
            if (inputStream == null) {
                return false;
            }
            Map<String,Object> mapFromJar = ConfigTypeHandler.toMap(getAtumMod(),
                    getType(),
                    ConfigTypeHandler.toString(inputStream)
            );
            //json somehow sees the integer as double
            int requiredVersion = (int) Double.parseDouble(
                    Objects.requireNonNullElse(mapFromJar.get("config_version"),
                            0).toString()
            );
            InputStreamReader reader = new InputStreamReader(file.toURI().toURL().openStream());
            String s = ConfigTypeHandler.readToString(reader);
            Map<String, Object> mapFromDir = ConfigTypeHandler.toMap(getAtumMod(), getType(), s);
            int currentVersion = (int) Double.parseDouble(
                    Objects.requireNonNullElse(mapFromDir.get("config_version"),
                            0).toString()
            );

            return requiredVersion != currentVersion;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }
    @Override
    public void createFile(boolean forceResourceLoad) {
        InputStream inputStream = getAtumMod().getClass().getResourceAsStream(getResourcePath());
        if(inputStream==null && forceResourceLoad) {
            throw new NullPointerException("file not found inside the resources folder of a plugin");
        }
        if (!file.exists()) {
            try {
                if(inputStream==null) {
                    file.createNewFile();
                    return;
                }
                Files.copy(inputStream, Paths.get(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reload() throws IOException {
        InputStreamReader reader = new InputStreamReader(file.toURI().toURL().openStream());
        String s = ConfigTypeHandler.readToString(reader);
        super.applyData(ConfigTypeHandler.toMap(getAtumMod(),getType(),s));
    }

    @Override
    public void save() throws IOException {
        if(file.delete()) {
            Files.write(file.toPath(),toPlaintext().getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
            );
        }
    }

    @Override
    public String getResourcePath() {
        String path = subDirectoryPath.replace(" ","").isEmpty() ?
                getFileName() : subDirectoryPath +File.pathSeparator+ getFileName();
        return File.pathSeparator + path;
    }

    @Override
    public String toPlaintext() {
        StringBuilder contents = new StringBuilder();
        for(String line : super.toPlaintext().split("\n")) {
            if(line.contains("\r\n")){
                for (String s : line.split("\r\n")) {
                    if(line.contains("\r")){
                        for (String s1 : line.split("\r")) {
                            if(s1.startsWith("#")) {
                                continue;
                            }
                            contents.append(s1).append("\n");
                        }
                        continue;
                    }
                    if(s.startsWith("#")) {
                        continue;
                    }
                    contents.append(s).append("\n");
                }
                continue;
            }
            if(line.contains("\r")){
                for (String s : line.split("\r")) {
                    if(line.contains("\r\n")){
                        for (String s1 : line.split("\r\n")) {
                            if(s1.startsWith("#")) {
                                continue;
                            }
                            contents.append(s1).append("\n");
                        }
                        continue;
                    }
                    if(s.startsWith("#")) {
                        continue;
                    }
                    contents.append(s).append("\n");
                }
                continue;
            }

            if(line.startsWith("#")) {
                continue;
            }
            contents.append(line).append("\n");
        }
        return contents.toString();
    }

}
