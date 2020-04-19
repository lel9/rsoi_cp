package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;

import java.util.List;

@Data
public class ListPatientOutShort {
    private List<PatientOutShort> patients;
}
