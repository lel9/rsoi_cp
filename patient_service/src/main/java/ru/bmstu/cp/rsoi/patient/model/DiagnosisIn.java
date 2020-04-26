package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DiagnosisIn {

    @NotNull(message = "Текст диагноза должен быть задан")
    @NotEmpty(message = "Текст диагноза не должен быть пуст")
    private String text;

}
