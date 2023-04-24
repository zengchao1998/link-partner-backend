package com.wut.self;

import com.wut.self.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zeng
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void test() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        valueOperations.set("zengString", "zeng1998");
        valueOperations.set("zengInt", 1);
        valueOperations.set("zengDouble", 2.0);

        User user = new User();
        user.setId(0L);
        user.setUsername("zeng");
        user.setUserAccount("zeng1998");
        valueOperations.set("zeng", user, 1, TimeUnit.DAYS);

        Object zeng = valueOperations.get("zengString");
        Assertions.assertEquals("zeng1998", (String) zeng);
        Object zengInt = valueOperations.get("zengInt");
        Assertions.assertEquals(1, (Integer) zengInt);
        Object zengDouble = valueOperations.get("zengDouble");
        Assertions.assertEquals(2.0, (Double) zengDouble);
        Object zeng1 = valueOperations.get("zeng");
        System.out.println(zeng1);

    }
}
