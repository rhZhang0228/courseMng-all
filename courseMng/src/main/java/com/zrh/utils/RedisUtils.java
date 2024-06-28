package com.zrh.utils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtils {
    public static void deleteAll(RedisTemplate redisTemplate, String key) {
        redisTemplate.delete(redisTemplate.keys(key + "*"));
    }

    public static <T> PagingResult<T> selectPage(RedisTemplate<String, T> redisTemplate,
                                                 String key, Integer offset, Integer limit) {
        Integer start = offset;
        Integer end = start + limit - 1;
        List<T> list = redisTemplate.opsForList().range(key, start, end);
        Long size = redisTemplate.opsForList().size(key);
        return new PagingResult<>(size, list);
    }

    public static <T> PagingResult<T> selectPageByCurrentPage(RedisTemplate<String, T> redisTemplate,
                                                              String key, Integer page, Integer pageSize) {
        Integer start = (page - 1) * pageSize;
        Integer end = start + pageSize - 1;
        List<T> list = redisTemplate.opsForList().range(key, start, end);
        Long size = redisTemplate.opsForList().size(key);
        return new PagingResult<>(size, list);
    }

    public static <T> void insertList(RedisTemplate<String, T> redisTemplate, List<T> list, String key) {
        if (CollectionUtils.isEmpty(list)) {
            log.debug("redis不缓存空字符串");
            return;
        }
        redisTemplate.opsForList().rightPushAll(key, list);
        redisTemplate.expire(key, RandomUtil.randomInt(30, 60), TimeUnit.MINUTES);
    }

    public static <T> List<T> selectList(RedisTemplate<String, T> redisTemplate, String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
