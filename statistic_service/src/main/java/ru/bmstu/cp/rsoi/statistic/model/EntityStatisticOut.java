package ru.bmstu.cp.rsoi.statistic.model;

import lombok.Data;

@Data
public class EntityStatisticOut {
    private String id;

    private Long createCount = 0L;
    private Long deleteCount = 0L;
    private Long readCount = 0L;
    private Long updateCount = 0L;

    private Long childCreateCount = 0L;
    private Long childDeleteCount = 0L;
    //private Long childReadCount = 0L;
    private Long childUpdateCount = 0L;
}
