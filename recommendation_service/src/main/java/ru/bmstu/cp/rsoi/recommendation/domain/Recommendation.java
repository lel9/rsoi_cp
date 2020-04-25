package ru.bmstu.cp.rsoi.recommendation.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "recommendations")
@Data
public class Recommendation implements Serializable {

    @Id
    private String id;

    private Long date;

    @Field(value = "drug_id")
    private String drugId;

    private User author;

    private String text;

}
