package ru.bmstu.cp.rsoi.statistic.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntityStatisticOut {
    private String entityId;
    private Counts counts;
    private List<Counts> childrenStatistic = new ArrayList<>();
}
