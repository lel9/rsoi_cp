package ru.bmstu.cp.rsoi.patient.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PatientOut {

    private String id;

    private String cardId;

    private Long birthday;

    private Character sex;

    private List<ReceptionOut> receptions = new ArrayList<>();

}
