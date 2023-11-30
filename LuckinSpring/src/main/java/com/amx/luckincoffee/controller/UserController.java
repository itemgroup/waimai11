package com.amx.luckincoffee.controller;

import com.amx.luckincoffee.entity.Result;
import com.amx.luckincoffee.entity.User;
import com.amx.luckincoffee.service.UserServiceImpl;
import com.amx.luckincoffee.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/user/login",produces = "application/json;charset=UTF-8")
    public Result login(@RequestBody User user){
        Result result = new Result();
        User userDB = userService.login(user);

        result.setCode(200);
        result.setMessage("登录成功");

        String telephone = user.getTelephone();

        if (userDB!=null){
            String userName = userDB.getName();
            String token = JwtUtil.createToken(userName, telephone);
            result.setData(token);
        }else {
            Random random = new Random();
            int num = random.nextInt(99999);
            String userName = "瑞幸用户_" + num;

            int i = userService.addUser(userName, telephone);

            String token = JwtUtil.createToken(userName, telephone);
            result.setData(token);
        }

        return result;
    }

    @GetMapping("/user/info")
    public Result getUserInfo(@RequestParam("token")String token){
        Result result = new Result();

        if(JwtUtil.verifyToken(token)) {
            String userName = JwtUtil.getUserName(token);
            String userTelephone = JwtUtil.getUserTelephone(token);
            Map<String, String> data = new HashMap<>();
            data.put("name", userName);
            data.put("telephone", userTelephone);

            result.setCode(200);
            result.setMessage("获取用户信息成功");
            result.setData(data);
        }else {
            result.setCode(500);
            result.setMessage("获取用户信息失败");
        }
        return result;
    }
}
