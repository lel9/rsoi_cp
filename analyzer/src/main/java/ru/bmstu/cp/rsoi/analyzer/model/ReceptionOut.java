package ru.bmstu.cp.rsoi.analyzer.model;

import lombok.Data;
import ru.bmstu.cp.rsoi.analyzer.model.StateOut;
import ru.bmstu.cp.rsoi.analyzer.model.drug.DrugOut;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceptionOut {

    private String date;

    private StateOut state;

}
