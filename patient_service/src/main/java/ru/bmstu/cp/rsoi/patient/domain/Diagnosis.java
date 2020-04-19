package ru.bmstu.cp.rsoi.patient.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Diagnosis {
    private String text;
}
