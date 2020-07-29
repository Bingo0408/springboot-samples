package org.bingo.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User1 {
    private Long id;
    private String name;

    public User1(String name) {
        this.name = name;
    }
}
