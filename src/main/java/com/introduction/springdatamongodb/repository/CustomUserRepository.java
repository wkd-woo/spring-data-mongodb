package com.introduction.springdatamongodb.repository;

import com.introduction.springdatamongodb.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomUserRepository {

    /**
     * Custom Query에 대한 이야기
     * https://stackoverflow.com/questions/48460066/mongorepository-findbythisandthat-custom-query-with-multiple-parameters
     */
    // public User findByNameAndOptionsAge(@Param("name") String name, @Param("age") Integer age);
    public List<User> findByNameAndAge(String name, Integer age);

    List<User> findAllByName(String name);

}
