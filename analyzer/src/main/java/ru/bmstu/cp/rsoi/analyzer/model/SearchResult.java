package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private String pid;

    private List<DrugOutShort> drug;

    private List<ReceptionOut> outcomes;

}
