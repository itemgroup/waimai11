package com.amx.luckincoffee.service;

import com.amx.luckincoffee.entity.User;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {
    User login(User user); // 登录接口

    int addUser(String name,String telephone); // 注册用户
}
