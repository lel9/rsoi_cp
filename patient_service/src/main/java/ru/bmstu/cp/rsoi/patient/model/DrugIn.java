package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DrugIn {

    @NotNull(message = "ID препарата должен быть задан")
    @NotEmpty(message = "ID препарата не должен быть пуст")
    private String id;

    @NotNull(message = "Название препарата должно быть задано")
    @NotEmpty(message = "Название препарата не должно быть пусто")
    private String tradeName;

}
