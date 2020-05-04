package ru.bmstu.cp.rsoi.patient.model.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagePatientOut {

    private Integer totalPages;

    private Long totalElements;

    private Integer page;

    private Integer size;

    private List<PatientOut> results;

}
