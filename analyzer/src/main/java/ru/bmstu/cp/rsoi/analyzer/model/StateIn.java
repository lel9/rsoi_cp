package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StateIn {

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
