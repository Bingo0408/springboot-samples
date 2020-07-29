package org.bingo.sample.service;

import org.bingo.sample.entity.User2;
import org.bingo.sample.mapper.User2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class User2ServiceImpl implements User2Service {

    @Autowired
    private User2Mapper mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void TX_PROPAGATION_REQUIRED_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void TX_PROPAGATION_SUPPORTS_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void TX_PROPAGATION_MANDATORY_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void TX_PROPAGATION_REQUIRES_NEW_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void TX_PROPAGATION_NOT_SUPPORTED_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void TX_PROPAGATION_NEVER_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void TX_PROPAGATION_NESTED_INSERT(User2 user2, boolean throwException) {
        mapper.insert(user2);
        if(throwException)
            throw new RuntimeException();
    }
}
