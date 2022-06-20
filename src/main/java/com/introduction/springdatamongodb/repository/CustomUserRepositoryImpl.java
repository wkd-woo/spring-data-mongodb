package com.introduction.springdatamongodb.repository;

import com.introduction.springdatamongodb.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#repositories.custom-implementations
 */
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * https://stackoverflow.com/questions/48460066/mongorepository-findbythisandthat-custom-query-with-multiple-parameters
     */

    @Override
    public List<User> findAllByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));

        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findByNameAndAge(String name, Integer age) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("name").is(name),
                Criteria.where("age").is(age)
        ));

        return mongoTemplate.find(query, User.class);
    }


}
