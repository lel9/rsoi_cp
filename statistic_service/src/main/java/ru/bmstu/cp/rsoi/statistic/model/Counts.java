package ru.bmstu.cp.rsoi.statistic.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Counts {
    private String entityName;
    private Long createCount = 0L;
    private Long deleteCount = 0L;
    private Long readCount = 0L;
    private Long updateCount = 0L;
}
