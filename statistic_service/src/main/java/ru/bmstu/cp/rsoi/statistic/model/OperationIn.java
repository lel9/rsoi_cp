package ru.bmstu.cp.rsoi.statistic.model;

import lombok.Data;

@Data
public class OperationIn {
    private String entityName;
    private String parentEntityName;
    private String entityId;
    private String parentEntityId;
    private String operationName;
    private Long date;
}
