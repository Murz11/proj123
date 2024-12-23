package ru.sfedu.tripshelperpack.utils;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YamlUtil {
    private static final Logger logger = Logger.getLogger(YamlUtil.class.getName());

    public static Map<String, Object> loadYaml(String filePath) {
        Map<String, Object> yamlMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentKey = null;
            Map<String, String> currentNestedMap = null;
            List<String> currentList = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    if (value.isEmpty()) {

                        currentKey = key;
                        if (yamlMap.containsKey(currentKey) && yamlMap.get(currentKey) instanceof List) {
                            currentList = (List<String>) yamlMap.get(currentKey);
                        } else {
                            currentNestedMap = new HashMap<>();
                            yamlMap.put(currentKey, currentNestedMap);
                        }
                    } else {

                        if (currentKey != null && yamlMap.get(currentKey) instanceof Map) {
                            ((Map<String, String>) yamlMap.get(currentKey)).put(key, value);
                        } else {
                            yamlMap.put(key, value);
                        }
                    }
                } else if (line.startsWith("-") && currentKey != null) {
                    if (yamlMap.get(currentKey) instanceof List) {
                        ((List<String>) yamlMap.get(currentKey)).add(line.substring(1).trim());
                    } else {
                        currentList = new ArrayList<>();
                        currentList.add(line.substring(1).trim());
                        yamlMap.put(currentKey, currentList);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "YAML file not found: " + filePath, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while reading YAML file: " + filePath, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while processing YAML file: " + filePath, e);
        }

        return yamlMap;
    }
}
