package com.introduction.springdatamongodb.repository;

import com.introduction.springdatamongodb.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
public class CustomRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll(userRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    void customTest() {
        List<User> userList = new ArrayList<>();

        User user1 = new User("호식이", 30);
        User user2 = new User("호식이", 30);
        User user3 = new User("호식이", 40);
        User user4 = new User("두식이", 30);
        User user5 = new User("두식이", 40);

        for (User user : Arrays.asList(user1, user2, user3, user4, user5)) {
            userList.add(user);
        }

        userRepository.saveAll(userList);

        List<User> suspect = userRepository.findByNameAndAge("호식이", 30);
        suspect.forEach(System.out::println);
        assertThat(suspect.size()).isEqualTo(2);

        List<User> suspect2 = userRepository.findByNameAndAge("두식이", 30);
        suspect2.forEach(System.out::println);
        assertThat(suspect2.size()).isEqualTo(1);

        List<User> 호식이 = userRepository.findAllByName("호식이");
        호식이.forEach(System.out::println);
        assertThat(호식이.size()).isEqualTo(3);

        List<User> 두식이 = userRepository.findAllByName("두식이");
        두식이.forEach(System.out::println);
        assertThat(두식이.size()).isEqualTo(2);
    }
}
