package com.example.myproject;


import com.example.myproject.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@SpringBootTest
public class DistributeLockTest extends MyprojectApplication {

    @Resource
    private UserController userController;

    @Test
    public void disLockTest(){
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            log.info("当前时间：{}", new Date(start));
            userController.getUsers();
            log.info("userController.getUsers()消耗时间：{}ms", System.currentTimeMillis() - start);
        }
    }
}
