package com.wut.self.jobs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wut.self.mapper.UserMapper;
import com.wut.self.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wut.self.constant.UserConstant.REDIS_KEY_PREFIX;

/**
 * @author zeng
 * 预缓存任务
 */
@Component
@Slf4j
public class PreCachingTarget {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 重点用户(配置文件动态改变)
     */
    private final static List<Long> mainUserList = Arrays.asList(1L, 2L);

    /**
     * 预热的数据量参数
     * PAGE_NUM = 1: 推荐用户的页码
     * PAGE_SIZE = 20: 推荐用户的单个页面用户数目
     */
    private final static Integer PAGE_NUM = 1;
    private final static Integer PAGE_SIZE = 20;

    /**
     * 预存储用户的推荐主页(每天的凌晨 23.59 执行任务)
     */
    @Scheduled(cron = "0 59 23 * * *")
    public void doCacheRecommendUsers() {
        // 1. 获取分布式锁
        String lockName = String.format(REDIS_KEY_PREFIX, "lock");
        RLock lock = redissonClient.getLock(lockName);
        /*
         * lock.tryLock 方法参数
         * waitTime: 设置为0，表示只竞争一次，竞争失败不等待(定时任务一天只执行一次)
         * leaseTime: 锁的过期时间
         */
        try {
            if(lock.tryLock(0, 30000L, TimeUnit.MILLISECONDS)) {
                // 获取到锁执行业务逻辑
                for (Long userId : mainUserList) {
                    ValueOperations<String, Object> redisOperations = redisTemplate.opsForValue();
                    String redisKey = String.format(REDIS_KEY_PREFIX, userId);
                    // 1.1 从数据库中获取数据
                    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                    Page<User> users = userMapper.selectPage(new Page<>(PAGE_NUM, PAGE_SIZE), userQueryWrapper);
                    // 1.2 将从数据库中获取的数据,同步到缓存中
                    try {
                        // 注意设置过期时间(毫秒、秒、分钟、天)
                        redisOperations.set(redisKey, users, 1, TimeUnit.DAYS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 2. 释放自己的锁资源(保证方法无论是否执行完毕，都执行锁资源释放)
            if(lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
