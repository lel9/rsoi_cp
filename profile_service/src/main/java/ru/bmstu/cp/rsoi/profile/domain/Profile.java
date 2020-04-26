package ru.bmstu.cp.rsoi.profile.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "profiles")
@Data
public class Profile implements Serializable {

    @Id
    private String name;

    @Field(value = "display_name")
    private String displayName;

    private String email;

    private String organization;

    private String profession;
}
