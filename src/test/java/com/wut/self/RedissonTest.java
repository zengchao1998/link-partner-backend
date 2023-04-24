package com.wut.self;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zeng
 */
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        // list
        RList<String> list = redissonClient.getList("test-list");
        list.add("zeng");
        System.out.println(list.get(0));
        // map
        RMap<Object, Object> map = redissonClient.getMap("test-map");
        map.put("username", "zeng");
        System.out.println(map.get("username"));
        // set
        RSet<Object> set = redissonClient.getSet("test-set");
        set.add("zeng");
        set.add("chao");
        set.forEach(System.out::println);

        list.remove(0);
        map.remove("username");
        set.remove("zeng");
        set.remove("chao");
    }
}
