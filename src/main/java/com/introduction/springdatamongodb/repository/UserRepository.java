package com.introduction.springdatamongodb.repository;

import com.introduction.springdatamongodb.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {


}
