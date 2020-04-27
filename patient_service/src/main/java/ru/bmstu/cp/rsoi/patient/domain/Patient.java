package ru.bmstu.cp.rsoi.patient.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.bmstu.cp.rsoi.patient.model.PatientIn;

import java.io.Serializable;

@Document(collection = "patients")
@Data
public class Patient implements Serializable {

    @Id
    private String id;

    private String cardId;

    private Long birthday;

    private Character sex;

}
