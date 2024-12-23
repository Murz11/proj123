package ru.sfedu.tripshelperpack;
import org.junit.jupiter.api.Test;
import ru.sfedu.tripshelperpack.utils.XmlUtil;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class XmlTest {
    @Test
    public void testLoadXml() throws Exception {
        String filePath = "src/main/resources/properties.xml";
        Map<String, Object> loadedXml = XmlUtil.loadXml(filePath);

        assertNotNull(loadedXml, "XML должен быть загружен");

        // Проверяем корневой элемент
        Map<String, Object> root = (Map<String, Object>) loadedXml.get("root");
        assertNotNull(root, "Корневой элемент 'root' не должен быть пустым");

        // Проверяем app
        Map<String, String> app = (Map<String, String>) root.get("app");
        assertNotNull(app, "Раздел 'app' не должен быть пустым");
        assertEquals("mongodb://localhost:27017", app.get("mongodb.connectionString"));
        assertEquals("trips_helper", app.get("mongodb.databaseName"));
        assertEquals("history_content", app.get("mongodb.collectionName"));

        // Проверяем planets
        Map<String, List<String>> planets = (Map<String, List<String>>) root.get("planets");
        assertNotNull(planets, "Раздел 'planets' не должен быть пустым");
        List<String> planetList = planets.get("planet");
        assertNotNull(planetList, "Список планет не должен быть пустым");
        assertEquals(List.of("Земля", "Марс", "Венера", "Юпитер"), planetList);

        // Проверяем months
        Map<String, String> months = (Map<String, String>) root.get("months");
        assertNotNull(months, "Раздел 'months' не должен быть пустым");

        // Проверка для каждого месяца
        assertEquals("Январь", months.get("1"));
        assertEquals("Февраль", months.get("2"));
        assertEquals("Март", months.get("3"));
        assertEquals("Апрель", months.get("4"));
    }

}
