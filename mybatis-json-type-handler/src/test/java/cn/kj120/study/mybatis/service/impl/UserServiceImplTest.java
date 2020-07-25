package cn.kj120.study.mybatis.service.impl;

import cn.kj120.study.mybatis.domain.Cate;
import cn.kj120.study.mybatis.domain.User;
import cn.kj120.study.mybatis.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        User user = new User();
        user.setAccount("potato");
        user.setAvatar("http://帅哥.jpg");
        user.setMobile("155********");
        user.setNickname("王路飞");

        Cate cate = new Cate();

        cate.setImage("http://wanglufei.img");
        cate.setName("海贼");
        cate.setPath("1,2,3");

        user.setCate(cate);

        this.user = user;
    }

    @Test
    void add() {
        userService.add(user);
    }

    @Test
    void addTypeHandler() {
        userService.addTypeHandler(user);
    }

    @Test
    void getUserById() {
        User userById = userService.getUserById(62);

        System.err.println(userById);
    }

    @Test
    void getUserByIdTypeHandler() {
        User userByIdTypeHandler = userService.getUserByIdTypeHandler(62);

        System.err.println(userByIdTypeHandler);
    }
}