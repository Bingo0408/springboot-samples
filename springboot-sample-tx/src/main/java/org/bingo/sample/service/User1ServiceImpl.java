package org.bingo.sample.service;

import org.bingo.sample.entity.User1;
import org.bingo.sample.mapper.User1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class User1ServiceImpl implements User1Service {

    @Autowired
    private User1Mapper mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void TX_REQUIRED(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void TX_SUPPORTS(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void TX_MANDATORY(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void TX_REQUIRES_NEW(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void TX_NOT_SUPPORTED(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void TX_NEVER(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void TX_NESTED(User1 user1, boolean throwException) {
        mapper.insert(user1);
        if(throwException)
            throw new RuntimeException();
    }
}
