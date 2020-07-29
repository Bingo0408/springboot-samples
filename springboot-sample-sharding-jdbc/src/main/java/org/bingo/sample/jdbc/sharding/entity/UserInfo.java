package org.bingo.sample.jdbc.sharding.entity;

import lombok.Data;

@Data
public class UserInfo {
    private Long userId;
    private String userName;
    private String account;
    private String password;
}
