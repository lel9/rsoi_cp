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

    private Long childTotalCreateCount = 0L;
    private Long childTotalDeleteCount = 0L;
    //private Long childTotalReadCount = 0L;
    private Long childTotalUpdateCount = 0L;

    private List<EntityStatisticOut> entitiesStatistic = new ArrayList<>();
    private String errMessage;
}