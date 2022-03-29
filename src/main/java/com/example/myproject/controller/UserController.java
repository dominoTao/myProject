package com.example.myproject.controller;

import com.example.myproject.annotation.DistributeLock;
import com.example.myproject.bean.User;
import com.example.myproject.service.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserInfo userInfo;

    @GetMapping(value = "/users")
    @DistributeLock(key = "distribute_lock_test")
    public List<User> getUsers(){
        final List<User> users = userInfo.userList();
        return users;
    }
}
