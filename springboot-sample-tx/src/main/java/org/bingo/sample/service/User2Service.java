package org.bingo.sample.service;

import org.bingo.sample.entity.User2;

public interface User2Service {

    void TX_PROPAGATION_REQUIRED_INSERT(User2 user2, boolean throwException);

    void TX_PROPAGATION_SUPPORTS_INSERT(User2 user2, boolean throwException);

    void TX_PROPAGATION_MANDATORY_INSERT(User2 user2, boolean throwException);

    void TX_PROPAGATION_REQUIRES_NEW_INSERT(User2 user2, boolean throwException);

    void TX_PROPAGATION_NOT_SUPPORTED_INSERT(User2 user2, boolean throwException);

    void TX_PROPAGATION_NEVER_INSERT(User2 user2, boolean throwException);

    void TX_PROPAGATION_NESTED_INSERT(User2 user2, boolean throwException);
}
