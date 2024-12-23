package ru.sfedu.tripshelperpack;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import ru.sfedu.tripshelperpack.models.HistoryContent;
import ru.sfedu.tripshelperpack.utils.MongoDBUtil;


import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MongoDBTest {

    private static String testId;

    @BeforeEach
    public void setUp() {
        HistoryContent content = new HistoryContent();
        content.setClassName("TestClass");
        content.setCreatedDate(new Date());
        content.setActor("TestUser");
        content.setMethodName("testCreateMethod");
        content.setParams("param1=value1");
        content.setResult("SUCCESS");

        MongoDBUtil.saveHistoryContent(content);
        List<HistoryContent> allContent = MongoDBUtil.getAllHistoryContent();
        testId = allContent.get(allContent.size() - 1).getId().toString();

    }

    @Test
    @Order(1)
    public void testCreate() {
        List<HistoryContent> allContent = MongoDBUtil.getAllHistoryContent();
        assertFalse(allContent.isEmpty(), "The database should not be empty after saving.");

    }

    @Test
    @Order(2)
    public void testReadById() {

        HistoryContent fetchedContent = MongoDBUtil.getHistoryContentById(testId);
        assertNotNull(fetchedContent, "Object fetched by ID should not be null.");
        assertEquals("TestClass", fetchedContent.getClassName(), "Class name should match.");

    }

    @Test
    @Order(3)
    public void testUpdate() {
        HistoryContent contentToUpdate = MongoDBUtil.getHistoryContentById(testId);
        assertNotNull(contentToUpdate, "Object to update should not be null.");

        contentToUpdate.setResult("FAULT");
        contentToUpdate.setMethodName("testUpdateMethod");

        boolean isUpdated = MongoDBUtil.updateHistoryContent(testId, contentToUpdate);
        assertTrue(isUpdated, "Object should be updated successfully.");


        HistoryContent updatedContent = MongoDBUtil.getHistoryContentById(testId);
        assertEquals("FAULT", updatedContent.getResult(), "Result field should be updated to 'ERROR'.");
        assertEquals("testUpdateMethod", updatedContent.getMethodName(), "Method name should be updated.");
    }

    @Test
    @Order(4)
    public void testReadAll() {
        List<HistoryContent> allContent = MongoDBUtil.getAllHistoryContent();
        assertFalse(allContent.isEmpty(), "The database should contain records.");
    }

    @Test
    @Order(5)
    public void testDelete() {
        boolean isDeleted = MongoDBUtil.deleteHistoryContent(testId);
        assertTrue(isDeleted, "Object should be deleted successfully.");

        HistoryContent deletedContent = MongoDBUtil.getHistoryContentById(testId);
        assertNull(deletedContent, "Object should not exist after deletion.");
    }
}