package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.Data;
import ru.bmstu.cp.rsoi.patient.model.StateOut;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceptionOut {

    private String id;

    private Long date;

    private StateOut state;

    private DiagnosisOut diagnosis;

    private List<DrugOut> drugs = new ArrayList<>();

}
