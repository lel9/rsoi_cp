package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.Data;

@Data
public class ReceptionIn {

    private StateOut state;

    private DiagnosisIn diagnosis;
}
