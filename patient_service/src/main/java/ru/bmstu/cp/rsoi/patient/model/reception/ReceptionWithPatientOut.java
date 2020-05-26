package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ReceptionWithPatientOut {

    private String id;

    private PatientIdOnlyOut patient;

    private String date;

    private StateOut state;

    private DiagnosisOut diagnosis;

    private List<DrugOut> drugs = new ArrayList<>();

}