package ru.sfedu.tripshelperpack.utils;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.types.ObjectId;
import ru.sfedu.tripshelperpack.Constants;
import ru.sfedu.tripshelperpack.models.HistoryContent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDBUtil {
    private static final Logger logger = Logger.getLogger(MongoDBUtil.class.getName());

    private static final String CONNECTION_STRING = ConfigurationUtil.getConfigurationEntry(Constants.MONGODB_CONNECTION_STRING_KEY);
    private static final String DATABASE_NAME = ConfigurationUtil.getConfigurationEntry(Constants.MONGODB_DATABASE_NAME_KEY);
    private static final String COLLECTION_NAME = ConfigurationUtil.getConfigurationEntry(Constants.MONGODB_COLLECTION_NAME_KEY);

    private static final MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    private static final MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

    private static MongoCollection<Document> getCollection() {
        return database.getCollection(COLLECTION_NAME);
    }

    public static void saveHistoryContent(HistoryContent content) {
        try {
            MongoCollection<Document> collection = getCollection();
            Document document = new Document("className", content.getClassName())
                    .append("createdDate", content.getCreatedDate())
                    .append("actor", content.getActor())
                    .append("methodName", content.getMethodName())
                    .append("params", content.getParams())
                    .append("result", content.getResult());
            if (content.getId() != null) {
                document.append("_id", content.getId());
            }
            collection.insertOne(document);
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Error while saving history content: " + e.getMessage(), e);
        }
    }

    public static List<HistoryContent> getAllHistoryContent() {
        List<HistoryContent> list = new ArrayList<>();
        try {
            MongoCollection<Document> collection = getCollection();
            for (Document doc : collection.find()) {
                list.add(documentToHistoryContent(doc));
            }
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Error while retrieving all history content: " + e.getMessage(), e);
        }
        return list;
    }

    public static HistoryContent getHistoryContentById(String id) {
        try {
            MongoCollection<Document> collection = getCollection();
            Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
            return (doc != null) ? documentToHistoryContent(doc) : null;
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Error while retrieving history content by ID: " + e.getMessage(), e);
            return null;
        }
    }

    public static boolean updateHistoryContent(String id, HistoryContent updatedContent) {
        try {
            MongoCollection<Document> collection = getCollection();
            Document updateDoc = new Document()
                    .append("className", updatedContent.getClassName())
                    .append("createdDate", updatedContent.getCreatedDate())
                    .append("actor", updatedContent.getActor())
                    .append("methodName", updatedContent.getMethodName())
                    .append("params", updatedContent.getParams())
                    .append("result", updatedContent.getResult());

            var result = collection.replaceOne(Filters.eq("_id", new ObjectId(id)), updateDoc);
            return result.getModifiedCount() > 0;
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Error while updating history content: " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean deleteHistoryContent(String id) {
        try {
            MongoCollection<Document> collection = getCollection();
            var result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
            return result.getDeletedCount() > 0;
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Error while deleting history content: " + e.getMessage(), e);
            return false;
        }
    }

    private static HistoryContent documentToHistoryContent(Document doc) {
        HistoryContent content = new HistoryContent();
        try {
            content.setId(doc.getObjectId("_id"));
            content.setClassName(doc.getString("className"));
            content.setCreatedDate(doc.getDate("createdDate"));
            content.setActor(doc.getString("actor"));
            content.setMethodName(doc.getString("methodName"));
            content.setParams(doc.getString("params"));
            content.setResult(doc.getString("result"));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while converting document to HistoryContent: " + e.getMessage(), e);
        }
        return content;
    }
}
