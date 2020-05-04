package ru.bmstu.cp.rsoi.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRecommendationOut {

    private Integer totalPages;

    private Long totalElements;

    private Integer page;

    private Integer size;

    private List<Recommendation> result;
}
