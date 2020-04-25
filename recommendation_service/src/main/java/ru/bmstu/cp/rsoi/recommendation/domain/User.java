package ru.bmstu.cp.rsoi.recommendation.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {

    private String id;

    private String displayName;

}
