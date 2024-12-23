package ru.sfedu.tripshelperpack.api;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataProviderXML<T> implements IDataProvider<T> {
    private static final Logger logger = LoggerFactory.getLogger(DataProviderXML.class);

    private final String filePath;
    private final Serializer serializer = new Persister();

    public DataProviderXML(String filePath, Class<T> type) {
        this.filePath = filePath;
    }

    @Override
    public void saveRecord(T record) throws Exception {
        try {
            File file = new File(filePath);

            Wrapper<T> wrapper = file.exists() ? serializer.read(Wrapper.class, file) : new Wrapper<>();
            wrapper.getRecords().add(record);

            serializer.write(wrapper, file);
            logger.info("Запись успешно сохранена: {}", record);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении записи: {}", record, e);
            throw new Exception("Ошибка при сохранении записи.", e);
        }
    }

    @Override
    public void deleteRecord(long id) throws Exception {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                logger.warn("Файл не найден: {}", filePath);
                throw new Exception("Файл не найден.");
            }

            Wrapper<T> wrapper = serializer.read(Wrapper.class, file);

            boolean deleted = wrapper.getRecords().removeIf(record -> {
                try {
                    return getIdFromRecord(record) == id;
                } catch (Exception e) {
                    logger.error("Ошибка при извлечении ID из записи", e);
                    return false;
                }
            });

            if (!deleted) {
                logger.warn("Запись с ID {} не найдена.", id);
                throw new Exception("Запись с ID " + id + " не найдена.");
            }

            serializer.write(wrapper, file);
            logger.info("Запись с ID {} успешно удалена.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении записи с ID {}", id, e);
            throw new Exception("Ошибка при удалении записи.", e);
        }
    }

    @Override
    public T getRecordById(long id) throws Exception {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                logger.warn("Файл не найден: {}", filePath);
                throw new Exception("Файл не найден.");
            }

            Wrapper<T> wrapper = serializer.read(Wrapper.class, file);

            return wrapper.getRecords().stream()
                    .filter(record -> {
                        try {
                            return getIdFromRecord(record) == id;
                        } catch (Exception e) {
                            logger.error("Ошибка при извлечении ID из записи", e);
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> {
                        logger.warn("Запись с ID {} не найдена.", id);
                        return new Exception("Запись с ID " + id + " не найдена.");
                    });
        } catch (Exception e) {
            logger.error("Ошибка при получении записи с ID {}", id, e);
            throw new Exception("Ошибка при получении записи.", e);
        }
    }

    @Override
    public List<T> initDataSource() throws Exception {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                logger.info("Файл не найден: {}. Создан пустой источник данных.", filePath);
                return new ArrayList<>();
            }

            Wrapper<T> wrapper = serializer.read(Wrapper.class, file);
            logger.info("Источник данных успешно инициализирован из файла: {}", filePath);
            return wrapper.getRecords();
        } catch (Exception e) {
            logger.error("Ошибка при инициализации источника данных.", e);
            throw new Exception("Ошибка при инициализации источника данных.", e);
        }
    }

    private long getIdFromRecord(T record) throws Exception {
        try {
            return (long) record.getClass().getMethod("getId").invoke(record);
        } catch (Exception e) {
            logger.error("Не удалось извлечь ID из записи: {}", record, e);
            throw new Exception("Не удалось извлечь ID из записи.", e);
        }
    }

    @Root(name = "wrapper")
    public static class Wrapper<T> {
        @ElementList(name = "records", inline = true, required = false)
        private List<T> records = new ArrayList<>();

        public List<T> getRecords() {
            return records;
        }

        public void setRecords(List<T> records) {
            this.records = records;
        }
    }
}
