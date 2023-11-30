package com.amx.luckincoffee.service;

import com.amx.luckincoffee.dao.UserDao;
import com.amx.luckincoffee.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserDao userDao;

    @Override
    public User login(User user) {
        return userDao.login(user);
    }

    @Override
    public int addUser(String name,String telephone) {
        return userDao.addUser(name,telephone);
    }
}
