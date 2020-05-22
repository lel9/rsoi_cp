package ru.bmstu.cp.rsoi.drug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDrugOut {
    private List<DrugOutShort> results;
}
