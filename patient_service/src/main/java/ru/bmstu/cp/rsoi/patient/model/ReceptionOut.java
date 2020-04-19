package ru.bmstu.cp.rsoi.patient.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceptionOut {

    @Id
    private String id;

    private Long date;

    private StateOut state;

    private DiagnosisOut diagnosis;

    private List<DrugOut> drugs = new ArrayList<>();

}
