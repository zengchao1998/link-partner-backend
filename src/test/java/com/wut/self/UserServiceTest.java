package com.wut.self;

import com.wut.self.model.domain.User;
import com.wut.self.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: zeng
 * Description:
 */
@SpringBootTest
@Slf4j
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("jerry");
        user.setUserAccount("jerry_test");
        user.setAvatarUrl("https://www.baidu.com");
        user.setGender(0);
        user.setUserPassword("jerry1234");
        user.setPhone("1344545");
        user.setEmail("1315@gmail.com");
        user.setUserStatus(0);
        boolean save = userService.save(user);
        System.out.println(save);
        Assertions.assertTrue(save);
        System.out.println(user.getId());
    }

    @Test
    void testSpecialCharacter() {
        String userAccount = "z111_";
        String validateStr = "^[a-zA-Z][\\w_]{3,}$";
        Matcher matcher = Pattern.compile(validateStr).matcher(userAccount);
        if(matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }

    @Test
    void testSearchUsersByTags() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assertions.assertNotNull(userList);
    }
}