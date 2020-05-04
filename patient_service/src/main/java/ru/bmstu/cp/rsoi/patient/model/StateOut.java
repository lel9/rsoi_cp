package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;

@Data
public class StateOut {

    private Character sex;

    private Integer years;

    private Integer months;

    private String lifeAnamnesis;

    private String diseaseAnamnesis;

    private String plaints;

    private String objectiveInspection;

    private String examinationsResults;

    private String specialistsConclusions;

}
