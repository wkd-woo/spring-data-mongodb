package com.introduction.springdatamongodb.domain;

import com.mongodb.client.result.UpdateResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Rollback(value = false)
class MongoTemplateTest {

    @Autowired
    MongoTemplate mongoTemplate;


    @BeforeEach
    void init() {
        User user = new User();
        user.setName("Jay");
        user.setAge(25);

        mongoTemplate.save(user, "users");
    }

    @Test
    void basic_test() {
        User user = new User();
        user.setName("Jay");
        user.setAge(25);

        System.out.println("user = " + user);
    }

    @Test
    void insert() {
        User user = new User();
        user.setName("Jay");
        user.setAge(25);

        User insert = mongoTemplate.insert(user, "users");

        System.out.println("user = " + user);
        System.out.println("insert = " + insert);
    }

    @Test
    void saveInsert() {
        // 새 객체를 생성하면 Save는 Insert와 같다.
        User user = new User();
        user.setName("Jay2");
        user.setAge(25);

        User save = mongoTemplate.save(user, "users");

        System.out.println("user = " + user);
        System.out.println("insert = " + save);
    }

    @Test
    void saveUpdate() {
        // 기존에 존재하는 객체. save 하면 dirty checking이 가능하다.
        // 대신 ObjectId를 Id로 사용해야 한다.
        User user = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Not Jay")), User.class);
        user.setName("is Jay");
        mongoTemplate.save(user, "users");
        System.out.println("user = " + user);
    }

    @Test
    void updateFirst() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Jay2"));
        Update update = new Update();
        update.set("name", "No More Jay");
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);

        System.out.println("updateResult = " + updateResult);
    }

    @Test
    void updateMulti() {

        // when
        User user0 = new User("Jay", 25);
        User user1 = new User("Michale", 25);
        User user2 = new User("Darwin", 25);
        User user3 = new User("Eugene", 45);
        User user4 = new User("Jordan", 31);

        /**
         * bulk save는 mongoTemplate에서는 지원X.
         * MongoRepository 상속받아 사용해야한다.
         */
        mongoTemplate.save(user0, "users");
        mongoTemplate.save(user1, "users");
        mongoTemplate.save(user2, "users");
        mongoTemplate.save(user3, "users");
        mongoTemplate.save(user4, "users");

        assertThat(user0.getAge()).isEqualTo(25);
        assertThat(user1.getAge()).isEqualTo(25);
        assertThat(user2.getAge()).isEqualTo(25);
        assertThat(user3.getAge()).isEqualTo(45);
        assertThat(user4.getAge()).isEqualTo(31);

        //then
        Query query = new Query();
        query.addCriteria(Criteria.where("age").is(25));
        Update update = new Update();
        update.set("age", 100);
        mongoTemplate.updateMulti(query, update, User.class);


        //result
        List<User> users = mongoTemplate.findAll(User.class);

        users.stream().forEach(user -> assertThat(user.getAge()).isNotEqualTo(25));
    }

    @Test
    void findAndModify() {

        User one = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Jay")), User.class);
        assertThat(one.getAge()).isEqualTo(100);
        System.out.println("one.getName() = " + one.getName());
        System.out.println("one.getAge() = " + one.getAge());

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Jay"));
        Update update = new Update();
        update.set("age", 26);

        User modify = mongoTemplate.findAndModify(query, update, User.class);

        // 이 상황에서는 테스트 통과 X. modify fetch가 되지 않았다.
        assertThat(modify.getAge()).isEqualTo(26);

        System.out.println("modify.getName() = " + modify.getName());
        System.out.println("modify.getAge() = " + modify.getAge());
    }

    /**
     * The upsert works on the find and modify else create semantics:
     * if the document is matched, update it,
     * or else create a new document by combining the query and update object.
     */
    @Test
    void upsert_whenMatched() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Darwin"));
        Update update = new Update();
        update.set("name", "Nunez");
        mongoTemplate.upsert(query, update, User.class);
    }

    @Test
    void upsert_whenUnMatched() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Fabio"));
        Update update = new Update();
        update.set("name", "Carvalho");
        mongoTemplate.upsert(query, update, User.class);
    }

    @Test
    void remove() {
        User user = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Michale")), User.class);
        mongoTemplate.remove(user, "user");
    }

}