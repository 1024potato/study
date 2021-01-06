package cn.kj120.study.spring.cache.expiration.demo.service.impl;

import cn.kj120.study.spring.cache.expiration.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl {

    private Map<Integer, User> userMap = new HashMap<>();

    {
        userMap.put(1 , new User(1, "王路飞"));
        userMap.put(2 , new User(2, "娜美桑"));
        userMap.put(3 , new User(3, "罗宾酱"));
        userMap.put(4 , new User(4, "卡二"));
    }

    @Cacheable(value = "user", key = "#id")
    public User find(Integer id) {

        User user = userMap.get(id);

        log.info("查询到用户 {}", user);

        return user;
    }
}
