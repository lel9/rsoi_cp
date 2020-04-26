package ru.bmstu.cp.rsoi.recommendation.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Profile {

    private String name;

    private String displayName;

}
