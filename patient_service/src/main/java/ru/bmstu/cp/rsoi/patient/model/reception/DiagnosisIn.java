package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DiagnosisIn {

    @NotBlank(message = "Текст диагноза не должен быть пуст")
    private String text;

}
