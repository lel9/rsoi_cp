package ru.bmstu.cp.rsoi.patient.model.patient;

import lombok.Data;
import ru.bmstu.cp.rsoi.patient.model.reception.ReceptionOut;

import java.util.ArrayList;
import java.util.List;

@Data
public class PatientWithReceptionsOut {

    private String id;

    private String cardId;

    private String birthday;

    private Character sex;

    private List<ReceptionOut> receptions = new ArrayList<>();

}
