package ru.bmstu.cp.rsoi.patient.model.patient;

import lombok.Data;

@Data
public class PatientIn {

    private String cardId;

    private Long birthday;

    private Character sex;
}