package org.bingo.sample.service;

import org.bingo.sample.entity.User1;

public interface User1Service {

    void TX_REQUIRED(User1 user1, boolean throwException);

    void TX_SUPPORTS(User1 user1, boolean throwException);

    void TX_MANDATORY(User1 user1, boolean throwException);

    void TX_REQUIRES_NEW(User1 user1, boolean throwException);

    void TX_NOT_SUPPORTED(User1 user1, boolean throwException);

    void TX_NEVER(User1 user1, boolean throwException);

    void TX_NESTED(User1 user1, boolean throwException);
}
