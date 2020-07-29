package org.bingo.mongo.repository.secondary;

import org.bingo.mongo.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecondaryUserRepository extends MongoRepository<UserEntity, Long> {
    UserEntity findByUserName(String username);
}
