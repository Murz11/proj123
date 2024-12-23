package ru.sfedu.tripshelperpack;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import ru.sfedu.tripshelperpack.utils.YamlUtil;
import static org.junit.jupiter.api.Assertions.*;

public class YamlTest {

    @Test
    public void testYamlParsing() throws IOException {
        String filePath = "src/main/resources/properties.yml";
        File yamlFile = new File(filePath);

        assertTrue(yamlFile.exists(), "YAML-файл должен существовать");


        Map<String, Object> yamlData = YamlUtil.loadYaml(filePath);

        Map<String, String> app = (Map<String, String>) yamlData.get("app");

        assertNotNull(app, "Секция 'app' должна быть определена");
        assertEquals("mongodb://localhost:27017", app.get("mongodb.connectionString"));
        assertEquals("trips_helper", app.get("mongodb.databaseName"));
        assertEquals("history_content", app.get("mongodb.collectionName"));


        List<String> planets = (List<String>) yamlData.get("planets");
        assertNotNull(planets, "Секция 'planets' должна быть определена");
        assertEquals(4, planets.size(), "Должно быть 4 планеты");
        assertTrue(planets.contains("Земля"), "Список планет должен содержать Землю");
        assertTrue(planets.contains("Марс"), "Список планет должен содержать Марс");


        Map<String, String> months = (Map<String, String>) yamlData.get("months");
        assertNotNull(months, "Секция 'months' должна быть определена");
        assertEquals("Январь", months.get("1"), "Январь должен быть привязан к ключу 1");
        assertEquals("Февраль", months.get("2"), "Февраль должен быть привязан к ключу 2");


    }

}