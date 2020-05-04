package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;

@Data
public class StateIn {

    private String lifeAnamnesis;

    private String diseaseAnamnesis;

    private String plaints;

    private String objectiveInspection;

    private String examinationsResults;

    private String specialistsConclusions;

}
