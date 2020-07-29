package org.bingo.mongo;

import org.bingo.mongo.model.UserEntity;
import org.bingo.mongo.repository.primary.PrimaryUserRepository;
import org.bingo.mongo.repository.secondary.SecondaryUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= MongodbSampleApplication.class)
public class UserRepositoryTest {

    @Autowired
    private PrimaryUserRepository primaryUserRepository;

    @Autowired
    private SecondaryUserRepository secondarySecondaryUserRepository;

    @Test
    public void testUserDao() {

        primaryUserRepository.save(new UserEntity(1l,"primaryRepository1", "password"));
        secondarySecondaryUserRepository.save(new UserEntity(1l,"secondaryRepository1", "password"));

        primaryUserRepository.save(new UserEntity(2l,"primaryRepository2", "password"));
        secondarySecondaryUserRepository.save(new UserEntity(2l,"secondaryRepository2", "password"));

        /** data in primary db */
        System.out.println(primaryUserRepository.findByUserName("primaryRepository1"));
        System.out.println(primaryUserRepository.findByUserName("primaryRepository2"));
        /** data in secondary db */
        System.out.println(secondarySecondaryUserRepository.findByUserName("secondaryRepository1"));
        System.out.println(secondarySecondaryUserRepository.findByUserName("secondaryRepository2"));
    }
}
