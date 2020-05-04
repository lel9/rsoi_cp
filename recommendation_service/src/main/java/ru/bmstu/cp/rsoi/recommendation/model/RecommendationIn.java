package ru.bmstu.cp.rsoi.recommendation.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RecommendationIn {

    @NotNull(message = "ID препарата должно быть задано")
    @NotEmpty(message = "ID препарата не должно быть пусто")
    private String drugId;

    @NotNull(message = "Текст рекомендации должен быть задан")
    @NotEmpty(message = "Текст рекомендации не должен быть пуст")
    private String text;
}
