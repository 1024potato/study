package cn.kj120.study.spring.cache.expiration.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Integer id;

    private String name;

    public User() {

    }
}
