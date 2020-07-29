package org.bingo.sample.drools;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private String type;
    private int discount;
}
