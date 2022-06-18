package com.introduction.springdatamongodb.domain;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    // @Id 어노테이션을 사용하면 ObjectId가 아닌 다른 타입도 사용가능.
    private ObjectId id; // ObjectId를 id로 사용하면 save - dirtyChecking 가능
    private String name;
    private Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
