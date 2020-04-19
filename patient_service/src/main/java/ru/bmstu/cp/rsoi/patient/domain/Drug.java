package ru.bmstu.cp.rsoi.patient.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
public class Drug {

    private String id;

    @Field(value = "tread_name")
    private String treadName;

}
