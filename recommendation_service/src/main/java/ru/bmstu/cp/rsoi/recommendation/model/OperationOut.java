package ru.bmstu.cp.rsoi.recommendation.model;

import lombok.Data;

@Data
public class OperationOut {
    private String entityName = "recommendation";
    private String parentEntityName = "drug";
    private String entityId;
    private String parentEntityId;
    private String operationName;
    private Long date = System.currentTimeMillis();

    public OperationOut(String entityId, String drugId, String operationName) {
        this.entityId = entityId;
        this.parentEntityId = drugId;
        this.operationName = operationName;
    }
}
