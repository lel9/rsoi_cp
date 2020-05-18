package ru.bmstu.cp.rsoi.patient.model.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListPatientOut {
    private List<PatientOut> results;
}
