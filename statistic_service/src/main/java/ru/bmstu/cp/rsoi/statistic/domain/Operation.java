package ru.bmstu.cp.rsoi.statistic.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "operations")
@Data
public class Operation {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    private String entityName;

    private String parentEntityName;

    private String entityId;

    private String parentEntityId;

    private String operationName;

    private Long date;
}
