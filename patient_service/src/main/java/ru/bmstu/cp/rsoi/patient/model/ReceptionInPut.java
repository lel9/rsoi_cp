package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;

import java.util.List;

@Data
public class ReceptionInPut {

    private Long date;

    private StateIn state;

    private DiagnosisIn diagnosis;

    private List<DrugIn> drugs;
}
