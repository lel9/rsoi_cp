package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

@Data
public class DrugIn {

    @NotBlank(message = "ID препарата не должен быть пуст")
    private String id;

    @NotBlank(message = "Название препарата не должно быть пусто")
    private String tradeName;

    private String releaseFormVSDosage;

    private String manufacturer;

}
