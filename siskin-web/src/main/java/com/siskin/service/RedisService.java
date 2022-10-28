package com.siskin.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String get(final String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Set<Object> getHashKey(final String key){
        return stringRedisTemplate.opsForHash().keys(key);
    }

    public List<Object> getHashValue(final String value){
        return stringRedisTemplate.opsForHash().values(value);
    }

    public HashMap<Object,Object> getHash(String key){
        HashMap<Object, Object> hashMap = new HashMap<>();
        Set<Object> keys = stringRedisTemplate.opsForHash().keys(key);
        List<Object> values = stringRedisTemplate.opsForHash().values(key);
        Iterator<Object> it = keys.iterator();
        for (int i = 0; i < keys.size(); i++) {
            hashMap.put(it.next(), values.get(i));
        }
        return hashMap;
    }

    public boolean set(final String key,String value){
        boolean flag = false;
        stringRedisTemplate.opsForValue().set(key, value);
        flag = true;
        return flag;
    }

    public boolean upDate(final String key,String value){
        boolean flag = false;
        stringRedisTemplate.opsForValue().getAndSet(key, value);
        flag = true;
        return flag;
    }

    public boolean delete(final String key){
        boolean flag = false;
        stringRedisTemplate.delete(key);
        flag = true;
        return flag;
    }

}
