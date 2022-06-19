package com.introduction.springdatamongodb.repository;

import com.introduction.springdatamongodb.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAllByName(String name);

}
