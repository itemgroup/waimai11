package com.amx.luckincoffee.dao;

import com.amx.luckincoffee.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface UserDao {
    User login(User user);

    int addUser(String name,String telephone);
}
