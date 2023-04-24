package com.wut.self.scripts;

import com.wut.self.mapper.UserMapper;
import com.wut.self.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

import static com.wut.self.constant.UserConstant.USER_AVATAR_DEFAULT_URL;

/**
 * @author zeng
 * 导入用户模拟数据
 */
// @Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    /**
     * 批量插入用户
     */
    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doInsertUser() {
        // spring 提供的计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1;
        for(int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("fake_user");
            user.setUserAccount("fake_user_test");
            user.setAvatarUrl(USER_AVATAR_DEFAULT_URL);
            user.setGender(0);
            user.setUserPassword("123456");
            user.setPhone("17564617831");
            user.setEmail("zcgmail@.com");
            user.setUserRole(0);
            user.setUserStatus(0);
            user.setValidateCode("100");
            user.setTags("[]");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
    }
}
