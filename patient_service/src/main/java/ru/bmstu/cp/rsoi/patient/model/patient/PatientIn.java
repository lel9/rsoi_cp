package ru.bmstu.cp.rsoi.patient.model.patient;

import lombok.Data;

@Data
public class PatientIn {

    private String cardId;

    private String birthday;

    private Character sex;
}
