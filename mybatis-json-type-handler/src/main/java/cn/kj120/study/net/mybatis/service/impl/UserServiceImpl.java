package cn.kj120.study.net.mybatis.service.impl;

import cn.kj120.study.mybatis.dao.UserDao;
import cn.kj120.study.mybatis.domain.User;
import cn.kj120.study.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer add(User user) {
        return userDao.add(user);
    }

    @Override
    public Integer addTypeHandler(User user) {
        return userDao.addTypeHandler(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByIdTypeHandler(Integer id) {
        return userDao.getUserByIdTypeHandler(id);
    }
}
