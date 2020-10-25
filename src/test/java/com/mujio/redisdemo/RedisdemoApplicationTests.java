package com.mujio.redisdemo;

import com.mujio.redisdemo.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisdemoApplicationTests {

    @Resource
    RedisUtil redisUtil;
    @Test
    void contextLoads() {
        boolean set = redisUtil.set("test", "this is a test");
        System.out.println(set);
        Object test = redisUtil.get("test");
        System.out.println(test);
    }

}
