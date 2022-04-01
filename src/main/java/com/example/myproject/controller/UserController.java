package com.example.myproject.controller;

import com.example.myproject.annotation.DistributeLock;
import com.example.myproject.annotation.WebLog;
import com.example.myproject.bean.User;
import com.example.myproject.service.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Resource
    private UserInfo userInfo;

    @GetMapping(value = "/users")
    @DistributeLock(key = "distribute_lock_test", timeout = 1, timeUnit = TimeUnit.MINUTES)
    @WebLog(value = "webLog测试", inDB = true)
    public List<User> getUsers(){
        List<User> users = userInfo.userList();
        return users;
    }
}
