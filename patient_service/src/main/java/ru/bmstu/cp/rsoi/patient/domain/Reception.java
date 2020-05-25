package ru.bmstu.cp.rsoi.patient.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "receptions")
@Data
public class Reception {

    @Id
    private String id;

    @DBRef
    private Patient patient;

    private String date;

    private State state;

    private Diagnosis diagnosis;

    private List<Drug> drugs;
}
