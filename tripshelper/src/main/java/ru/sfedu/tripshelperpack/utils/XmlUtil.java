package ru.sfedu.tripshelperpack.utils;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlUtil {

    private static final Logger logger = Logger.getLogger(XmlUtil.class.getName());

    public static Map<String, Object> loadXml(String filePath) {
        Map<String, Object> xmlMap = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));
            Element root = doc.getDocumentElement();
            xmlMap.put(root.getTagName(), processElement(root)); // Recursively process the root element
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading XML file: " + filePath, e);
        }
        return xmlMap;
    }

    private static Object processElement(Element element) {
        NodeList childNodes = element.getChildNodes();
        Map<String, Object> resultMap = new HashMap<>();
        List<Object> resultList = new ArrayList<>();
        boolean isList = false;

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                Object processedChild = processElement(childElement);

                // If the child element is a 'month' element, treat it as a map entry
                if ("month".equals(childElement.getTagName())) {
                    String key = childElement.getAttribute("key");
                    String value = childElement.getTextContent();
                    if (key != null && !key.isEmpty()) {
                        resultMap.put(key, value);
                    }
                } else {
                    // Otherwise, handle duplicate tag names as a list
                    if (resultMap.containsKey(childElement.getTagName())) {
                        isList = true;
                        if (!(resultMap.get(childElement.getTagName()) instanceof List)) {
                            List<Object> newList = new ArrayList<>();
                            newList.add(resultMap.get(childElement.getTagName()));
                            resultMap.put(childElement.getTagName(), newList);
                        }
                        ((List<Object>) resultMap.get(childElement.getTagName())).add(processedChild);
                    } else {
                        resultMap.put(childElement.getTagName(), processedChild);
                    }
                }
            }
        }

        // If no child elements, return either text content or an empty map
        if (resultMap.isEmpty()) {
            String textContent = element.getTextContent().trim();
            return textContent.isEmpty() ? new HashMap<>() : textContent;
        }

        // If the element is determined to be a list, return the list
        return isList && !resultList.isEmpty() ? resultList : resultMap;
    }

    public static void main(String[] args) {
        try {
            String filePath = "example.xml"; // Replace with your XML file path
            Map<String, Object> xmlData = loadXml(filePath);
            System.out.println("Parsed XML Data: " + xmlData);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in processing XML file", e);
        }
    }
}
