package ru.bmstu.cp.rsoi.profile.model;

import lombok.Data;

@Data
public class OperationOut {
    private String entityName = "user";
    private String parentEntityName = null;
    private String entityId;
    private String parentEntityId = null;
    private String operationName;
    private Long date = System.currentTimeMillis();

    public OperationOut(String entityId, String operationName) {
        this.entityId = entityId;
        this.operationName = operationName;
    }
}
