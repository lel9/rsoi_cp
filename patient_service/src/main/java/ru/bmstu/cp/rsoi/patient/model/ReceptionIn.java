package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ReceptionIn {

    private Long date;

    private StateIn state;

    private DiagnosisIn diagnosis;

    private List<DrugIn> drugs;

}
