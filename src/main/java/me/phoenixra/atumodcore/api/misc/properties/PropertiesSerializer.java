package me.phoenixra.atumodcore.api.misc.properties;

import me.phoenixra.atumodcore.api.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropertiesSerializer {
    public static PropertiesSet getProperties(String filePath) {
        File f = new File(filePath);
        if(!f.exists() || !f.isFile()) return null;
        List<String> lines = FileUtils.getFileLines(f);
        List<PropertiesSection> data = new ArrayList<>();
        String propertiesType = null;
        PropertiesSection currentData = null;
        boolean insideData = false;

        for (String s : lines) {
            String comp = s.replace(" ", "");
            //Setting the type of the PropertiesSet
            if (comp.startsWith("type=") && !insideData) {
                propertiesType = comp.split("=", 2)[1];
                continue;
            }

            //Starting a new data section
            if (comp.endsWith("{")) {
                if (!insideData) {
                    insideData = true;
                } else {
                    System.out.println("######################### WARNING #########################");
                    System.out.println("Invalid properties found in '" + filePath + "'! (Leaking properties section; Missing '}')");
                    System.out.println("###########################################################");
                    data.add(currentData);
                }
                currentData = new PropertiesSection(comp.split("[{]")[0]);
                continue;
            }

            //Finishing the data section
            if (comp.startsWith("}") && insideData) {
                data.add(currentData);
                insideData = false;
                continue;
            }

            //Collecting all entries inside the data section
            if (insideData && comp.contains("=")) {
                String value = s.split("=", 2)[1];
                if (value.startsWith(" ")) {
                    value = value.substring(1);
                }
                currentData.addEntry(comp.split("=", 2)[0], value);
            }
        }

        if (propertiesType == null) {
            System.out.println("######################### WARNING #########################");
            System.out.println("Invalid properties file found: " + filePath + " (Missing properties type)");
            System.out.println("###########################################################");
            return null;
        }
        PropertiesSet set = new PropertiesSet(propertiesType);
        for (PropertiesSection d : data) {
            set.addProperties(d);
        }

        return set;
    }

    public static void writeProperties(PropertiesSet props, String path) {
        try {
            List<PropertiesSection> l = props.getProperties();

            File f = new File(path);
            //check if path is a file
            if (f.getName().contains(".") && !f.getName().startsWith(".")) {
                File parent = f.getParentFile();
                if ((parent != null) && parent.isDirectory() && !parent.exists()) {
                    parent.mkdirs();
                }

                f.createNewFile();

                StringBuilder data = new StringBuilder();

                data.append("type = ").append(props.getPropertiesType()).append("\n\n");

                for (PropertiesSection ps : l) {
                    data.append(ps.getSectionType()).append(" {\n");
                    for (Map.Entry<String, String> e : ps.getEntries().entrySet()) {
                        data.append("  ").append(e.getKey()).append(" = ").append(e.getValue()).append("\n");
                    }
                    data.append("}\n\n");
                }

                FileUtils.writeTextToFile(f, false, data.toString());
            } else {
                System.out.println("############### CANNOT WRITE PROPERTIES! PATH IS NOT A FILE!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
