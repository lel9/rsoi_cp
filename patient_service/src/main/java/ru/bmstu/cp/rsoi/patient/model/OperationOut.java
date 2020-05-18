package ru.bmstu.cp.rsoi.patient.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class OperationOut {
    private String entityName;
    private String parentEntityName;
    private String entityId;
    private String parentEntityId;
    private String operationName;
    private Long date = System.currentTimeMillis();

    public OperationOut(String entityId, String operationName) {
        this.entityId = entityId;
        this.operationName = operationName;
    }

    public static OperationOut getPatientOperation(String entityId, String operationName) {
        return new OperationOut("patient", null, entityId, null, operationName);
    }

    public static OperationOut getReceptionOperation(String entityId, String patientId, String operationName) {
        return new OperationOut("reception", "patient", entityId, patientId, operationName);
    }

    private OperationOut(String entityName,
                         String parentEntityName,
                         String entityId,
                         String parentEntityId,
                         String operationName) {
        this.entityName = entityName;
        this.parentEntityName = parentEntityName;
        this.entityId = entityId;
        this.parentEntityId = parentEntityId;
        this.operationName = operationName;
    }
}
