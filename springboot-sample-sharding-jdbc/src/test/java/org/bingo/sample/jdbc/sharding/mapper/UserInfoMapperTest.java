package org.bingo.sample.jdbc.sharding.mapper;

import org.bingo.sample.jdbc.sharding.entity.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoMapperTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void select() {
        System.out.println(userInfoMapper.select(112l));
    }

    @Test
    public void insert() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(112l);
        userInfo.setAccount("Account");
        userInfo.setPassword("Password");
        userInfo.setUserName("Name");
        userInfoMapper.insert(userInfo);
    }
}