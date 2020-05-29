package ru.bmstu.cp.rsoi.analyzer.model.reception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bmstu.cp.rsoi.analyzer.model.StateOut;
import ru.bmstu.cp.rsoi.analyzer.model.drug.DrugOut;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionWithPatientOut {

    private String id;

    private PatientIdOnlyOut patient;

    private String date;

    private StateOut state;

    private DiagnosisOut diagnosis;

    private List<DrugOut> drugs = new ArrayList<>();
}