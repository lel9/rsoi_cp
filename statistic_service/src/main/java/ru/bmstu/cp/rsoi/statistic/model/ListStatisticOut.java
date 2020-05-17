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
    private Counts totalCounts;
    private List<Counts> childrenTotalCount;
    private List<EntityStatisticOut> entityiesStatistic = new ArrayList<>();
    private String errMessage;
}