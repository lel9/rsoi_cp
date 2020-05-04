package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;

import java.util.List;

@Data
public class ReceptionIn {

    private Long date;

    private StateIn state;

    private DiagnosisIn diagnosis;

    private List<DrugIn> drugs;

}
