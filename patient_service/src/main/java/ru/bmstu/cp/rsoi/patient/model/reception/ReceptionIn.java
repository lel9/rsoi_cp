package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;

import java.util.List;

@Data
public class ReceptionIn {

    private String date;

    private StateIn state;

    private DiagnosisIn diagnosis;

    private List<DrugIn> drugs;

}
