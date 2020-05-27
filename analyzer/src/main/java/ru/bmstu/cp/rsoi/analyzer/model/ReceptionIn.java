package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.Data;

@Data
public class ReceptionIn {

    private StateIn state;

    private DiagnosisIn diagnosis;
}
