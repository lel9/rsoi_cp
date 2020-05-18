package ru.bmstu.cp.rsoi.statistic.model;

import lombok.Data;

@Data
public class EntityStatisticOut {
    private String entityId;

    private Long createCount = 0L;
    private Long deleteCount = 0L;
    private Long readCount = 0L;
    private Long updateCount = 0L;

    private Long child1CreateCount = 0L;
    private Long child1DeleteCount = 0L;
    private Long child1ReadCount = 0L;
    private Long child1UpdateCount = 0L;
}
