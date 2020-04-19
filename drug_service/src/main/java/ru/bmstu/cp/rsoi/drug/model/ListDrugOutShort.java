package ru.bmstu.cp.rsoi.drug.model;

import lombok.Data;

import java.util.List;

@Data
public class ListDrugOutShort {
    private List<DrugOutShort> drugs;
}
