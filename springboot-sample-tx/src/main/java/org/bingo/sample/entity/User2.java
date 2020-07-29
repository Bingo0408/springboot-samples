package org.bingo.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User2 {
    private Long id;
    private String name;

    public User2(String name) {
        this.name = name;
    }
}
