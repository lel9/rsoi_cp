package ru.bmstu.cp.rsoi.statistic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counts {
    private String entityName;
    private Long createCount;
    private Long deleteCount;
    private Long readCount;
    private Long updateCount;
}
