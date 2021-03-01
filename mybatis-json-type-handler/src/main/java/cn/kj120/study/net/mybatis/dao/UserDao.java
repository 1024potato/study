package cn.kj120.study.net.mybatis.dao;

import cn.kj120.study.mybatis.domain.User;
import cn.kj120.study.net.mybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    Integer add(@Param("user") User user);

    Integer addTypeHandler(@Param("user") User user);

    User getUserById(Integer id);

    User getUserByIdTypeHandler(Integer id);
}
