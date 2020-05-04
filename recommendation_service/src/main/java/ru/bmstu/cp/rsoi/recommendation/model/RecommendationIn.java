package ru.bmstu.cp.rsoi.recommendation.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RecommendationIn {

    @NotBlank(message = "ID препарата не должно быть пусто")
    private String drugId;

    @NotBlank(message = "Текст рекомендации не должен быть пуст")
    private String text;
}
