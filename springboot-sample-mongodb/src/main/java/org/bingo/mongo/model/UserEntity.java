package org.bingo.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Document(collection = "user")
public class UserEntity implements Serializable{
    @Id
    private Long id;
    private String userName;
    private String password;
}
