package com.n23.expense_sharing_app;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {


    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    void testSendMail(){
//        redisTemplate.opsForValue().set("email","nisarg@gmail.com");

//        Object email = redisTemplate.opsForValue().get

        Object email = redisTemplate.opsForValue().get("email");
    }
}
