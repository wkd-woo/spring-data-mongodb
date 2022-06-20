package com.introduction.springdatamongodb.repository;

import com.introduction.springdatamongodb.domain.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.Rollback;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void init() {

    }


    @Test
    void insert() {
        User user = new User();
        user.setName("Jay");
        user.setAge(25);

        User inserted = userRepository.insert(user);
        System.out.println("inserted = " + inserted);
    }

    @Test
    void saveInsert() {
        User user = new User();
        user.setName("Alex");
        user.setAge(34);

        User save = userRepository.save(user);
        System.out.println("save = " + save);

    }

    @Test
    void saveUpdate() {
        User user = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("Alex")), User.class);

        user.setName("Jim");
        userRepository.save(user);
    }

    @Test
    void delete() {
        User user = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("Jim")), User.class);

        userRepository.delete(user);
    }

    @Test
    void findOne() {
        User user = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("Jay")), User.class);

        Optional<User> byId = userRepository.findById(user.getId());

        if (byId.isPresent()) {
            User user1 = byId.get();
            System.out.println("user1 = " + user1.getName());
            System.out.println("user1 = " + user1.getAge());
        }
    }

    @Test
    void exists() {
        User user = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("Jay")), User.class);


        //userRepository.exists((user.getId()));
        System.out.println(user.getId());
    }

    @Test
    void findAllWithSort() {
        List<User> userList = new ArrayList<>();
        User user1 = new User("A", 1);
        User user2 = new User("B", 2);
        User user3 = new User("C", 3);
        User user4 = new User("D", 4);
        User user5 = new User("E", 5);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);

        userRepository.saveAll(userList);

        userRepository.findAll(Sort.by(Sort.Direction.ASC, "age")).forEach(user -> {
            System.out.println("user = " + user.getId());
            System.out.println("user = " + user.getName());
            System.out.println("user = " + user.getAge());
        });
    }

    @Test
    void findAllWithPageable() {
        Pageable pageableRequest = PageRequest.of(3, 3);
        Page<User> page = userRepository.findAll(pageableRequest);
        List<User> users = page.getContent();

        users.stream().map(user -> "user = " + user).forEach(System.out::println);
    }


}