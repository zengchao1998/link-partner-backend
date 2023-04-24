package com.wut.self;

import com.wut.self.mapper.UserMapper;
import com.wut.self.model.domain.User;
import com.wut.self.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.wut.self.constant.UserConstant.USER_AVATAR_DEFAULT_URL;

/**
 * @author zeng
 */
@SpringBootTest
public class InsertUserTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    /**
     * 线程池
     * 1. Cpu 密集型: 分配的线程数目 = Cpu - 1
     * 2. IO 密集型: 分配的线程数目可以大于 Cpu 个数
     */
    private final ExecutorService executorService =
            new ThreadPoolExecutor(16, 1000, 10000,
                    TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 插入用户
     */
    @Test
    public void doInsertUsers() {
        // spring 提供的计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
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

    /**
     * 批量插入用户
     */
    @Test
    public void doInsertUsersByBatch() {
        // spring 提供的计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        List<User> users = new ArrayList<User>();
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
            users.add(user);
        }
        userService.saveBatch(users, 10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
    }

    /**
     * 多线程批量插入用户
     */
    @Test
    public void doInsertUsersByThreads() {
        // spring 提供的计时工具类
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 200000;
        final int BATCH_SIZE = 5000;
        int j = 0;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        // 多线程执行
        for (int i = 0; i < INSERT_NUM / BATCH_SIZE; i++) {
            List<User> users = new ArrayList<User>();
            while(true) {
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
                users.add(user);
                j++;
                if(j % BATCH_SIZE == 0) {
                    break;
                }
            }
            // 开启异步任务，执行具体的插入操作(每添加10000条数据，就会启动一个线程执行当前异步任务)
            // 指定线程池对象 executorService
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("ThreadName: " + Thread.currentThread().getName());
                userService.saveBatch(users, BATCH_SIZE);
            }, executorService);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
    }
}
