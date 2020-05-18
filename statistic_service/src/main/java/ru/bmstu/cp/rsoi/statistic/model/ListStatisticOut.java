package ru.bmstu.cp.rsoi.statistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListStatisticOut {
    private Long totalCreateCount = 0L;
    private Long totalDeleteCount = 0L;
    private Long totalReadCount = 0L;
    private Long totalUpdateCount = 0L;

    private Long child1TotalCreateCount = 0L;
    private Long child1TotalDeleteCount = 0L;
    private Long child1TotalReadCount = 0L;
    private Long child1TotalUpdateCount = 0L;

    private List<EntityStatisticOut> entitiesStatistic = new ArrayList<>();
    private String errMessage;
}