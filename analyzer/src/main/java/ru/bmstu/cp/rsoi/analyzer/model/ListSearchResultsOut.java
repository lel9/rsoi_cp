package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSearchResultsOut {
    private List<SearchResult> results;
}
