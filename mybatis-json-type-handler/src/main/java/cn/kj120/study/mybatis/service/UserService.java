package cn.kj120.study.mybatis.service;

import cn.kj120.study.mybatis.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

public interface UserService {

    Integer add(User user);

    Integer addTypeHandler(User user);

    User getUserById(Integer id);

    User getUserByIdTypeHandler(Integer id);
}
