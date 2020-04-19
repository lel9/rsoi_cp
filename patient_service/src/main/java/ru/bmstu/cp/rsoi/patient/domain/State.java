package ru.bmstu.cp.rsoi.patient.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class State {

    private Character sex;

    private Integer years;

    private Integer months;

    @Field(value = "life_anamnesis")
    private String lifeAnamnesis;

    @Field(value = "disease_anamnesis")
    private String diseaseAnamnesis;

    private String plaints;

    @Field(value = "objective_inspection")
    private String objectiveInspection;

    @Field(value = "examinations_results")
    private String examinationsResults;

    @Field(value = "specialists_conclusions")
    private String specialistsConclusions;

}
