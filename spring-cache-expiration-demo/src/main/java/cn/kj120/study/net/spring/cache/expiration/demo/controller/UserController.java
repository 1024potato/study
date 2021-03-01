package cn.kj120.study.net.spring.cache.expiration.demo.controller;


import cn.kj120.study.net.spring.cache.expiration.demo.entity.User;
import cn.kj120.study.spring.cache.expiration.demo.entity.User;
import cn.kj120.study.spring.cache.expiration.demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/{id}")
    public User find(@PathVariable("id") Integer id) {
        return userService.find(id);
    }
}
