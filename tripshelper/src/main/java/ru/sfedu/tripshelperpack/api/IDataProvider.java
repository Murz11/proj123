package ru.sfedu.tripshelperpack.api;

import java.util.List;

public interface IDataProvider<T> {
    void saveRecord(T record) throws Exception;
    void deleteRecord(long id) throws Exception;
    T getRecordById(long id) throws Exception;
    List<T> initDataSource() throws Exception;
}
