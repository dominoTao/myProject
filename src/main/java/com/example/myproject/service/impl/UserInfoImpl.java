package com.example.myproject.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.myproject.bean.User;
import com.example.myproject.mapper.UserMapper;
import com.example.myproject.service.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserInfoImpl implements UserInfo {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> userList() {
        final List<User> users = userMapper.selectList(null);
        log.info("用户列表：{}", JSON.toJSONString(users));
        return users;
    }
}
