package ru.bmstu.cp.rsoi.drug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDrugOut {

    private Integer totalPages;

    private Long totalElements;

    private Integer page;

    private Integer size;

    private List<DrugOutShort> results;

}
