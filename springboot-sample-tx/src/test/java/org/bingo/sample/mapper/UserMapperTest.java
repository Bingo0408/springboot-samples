package org.bingo.sample.mapper;

import org.bingo.sample.service.ParentService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private User1Mapper user1Mapper;
    @Autowired
    private User2Mapper user2Mapper;

    @Autowired
    private ParentService parentService;

    @Before
    public void clearEnv(){
        user1Mapper.deleteAll();
        user2Mapper.deleteAll();
    }
    @After
    public void showResult(){
        System.out.println("=============== Show  Result ===============");
        System.out.println(user1Mapper.selectAll());
        System.out.println(user2Mapper.selectAll());
    }

}
