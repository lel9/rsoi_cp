package ru.bmstu.cp.rsoi.patient.model.reception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListReceptionOut {
    private List<ReceptionOut> receptions = new ArrayList<>();
}