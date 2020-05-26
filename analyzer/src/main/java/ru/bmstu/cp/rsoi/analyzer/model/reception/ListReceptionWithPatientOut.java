package ru.bmstu.cp.rsoi.analyzer.model.reception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListReceptionWithPatientOut {
    private List<ReceptionWithPatientOut> results = new ArrayList<>();
}