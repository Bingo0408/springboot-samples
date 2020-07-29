package org.bingo.mongo.repository.primary;

import org.bingo.mongo.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrimaryUserRepository extends MongoRepository<UserEntity, Long> {
    UserEntity findByUserName(String username);
}
