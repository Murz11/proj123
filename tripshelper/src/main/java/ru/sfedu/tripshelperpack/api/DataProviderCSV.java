package ru.sfedu.tripshelperpack.api;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


public class DataProviderCSV<T> implements IDataProvider<T> {
    private static final Logger logger = LoggerFactory.getLogger(DataProviderCSV.class);
    private final String filePath;
    private final Class<T> type;

    public DataProviderCSV(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
    }

    @Override
    public void saveRecord(T record) throws Exception {
        File file = new File(filePath);
        boolean fileExists = file.exists();
        logger.info("Файл существует: {}", fileExists);

        try (FileWriter writer = new FileWriter(file, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
             CSVWriter csvWriter = new CSVWriter(bufferedWriter)) {

            ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(type);

            List<String> columnMapping = getFieldNames();
            strategy.setColumnMapping(columnMapping.toArray(new String[0]));

            if (!fileExists) {
                String[] headers = columnMapping.toArray(new String[0]);
                logger.info("Записываю заголовки: {}", Arrays.toString(headers));
                csvWriter.writeNext(headers);
            }

            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
                    .withMappingStrategy(strategy)
                    .withApplyQuotesToAll(false)
                    .build();

            logger.info("Записываю запись: {}", record);
            beanToCsv.write(record);
            logger.info("Запись успешно сохранена.");
        } catch (Exception e) {
            logger.error("Ошибка при сохранении записи: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteRecord(long id) throws Exception {
        try {
            List<T> records = initDataSource();
            logger.info("Загружены записи для удаления.");

            boolean deleted = records.removeIf(record -> {
                try {
                    return getIdFromRecord(record) == id;
                } catch (Exception e) {
                    logger.error("Ошибка при извлечении ID из записи для удаления: {}", e.getMessage());
                    return false;
                }
            });

            if (!deleted) {
                throw new Exception("Запись с ID " + id + " не найдена");
            }

            try (Writer writer = new FileWriter(filePath)) {
                StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
                beanToCsv.write(records);
                logger.info("Запись с ID {} успешно удалена.", id);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении записи: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public T getRecordById(long id) throws Exception {
        try {
            List<T> records = initDataSource();
            logger.info("Загружены записи для поиска по ID.");

            return records.stream()
                    .filter(record -> {
                        try {
                            return getIdFromRecord(record) == id;
                        } catch (Exception e) {
                            logger.error("Ошибка при извлечении ID из записи для поиска: {}", e.getMessage());
                            return false;
                        }
                    })
                    .findFirst()
                    .orElseThrow(() -> new Exception("Запись с ID " + id + " не найдена"));
        } catch (Exception e) {
            logger.error("Ошибка при поиске записи: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<T> initDataSource() throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            logger.info("Файл не существует.");
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(filePath)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<T> records = csvToBean.parse();

            logger.info("Загруженные записи: ");
            records.forEach(record -> logger.info("Запись: {}", record));

            return records;
        } catch (Exception e) {
            logger.error("Ошибка при загрузке данных из файла: {}", e.getMessage());
            throw e;
        }
    }

    private long getIdFromRecord(T record) throws Exception {
        try {
            return (long) record.getClass().getMethod("getId").invoke(record);
        } catch (Exception e) {
            logger.error("Ошибка при извлечении ID из записи: {}", e.getMessage());
            throw new Exception("Не удалось извлечь ID из записи", e);
        }
    }

    private List<String> getFieldNames() {
        Field[] fields = type.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }
}
