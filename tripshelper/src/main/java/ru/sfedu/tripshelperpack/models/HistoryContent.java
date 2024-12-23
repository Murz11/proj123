package ru.sfedu.tripshelperpack.models;

import org.bson.types.ObjectId;

import java.util.Date;

public class HistoryContent {

    private ObjectId id;
    private String className;
    private Date createdDate;
    private String actor;
    private String methodName;
    private String params;
    private OperationResult result;



    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result.toString();
    }

    public void setResult(String result) {
        this.result = OperationResult.valueOf(result);
    }

}

