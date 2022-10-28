package com.siskin.controller;

import com.siskin.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/redis")
public class RedisController{

    @Autowired
    private RedisService redisService;

    @GetMapping("get/{key}")
    public String get(@PathVariable("key")String key){
        return redisService.get(key);
    }

    @GetMapping("getHashKey/{key}")
    public Set<Object> getHashKey(@PathVariable String key){
        return redisService.getHashKey(key);
    }

    @GetMapping("getHash/{key}")
    public HashMap<Object,Object> getHash(@PathVariable String key){
        return redisService.getHash(key);
    }

    @PostMapping("set/{key}/{value}")
    public String set(@PathVariable("key")String key,@PathVariable("value")String value){
        redisService.set(key,value);
        return "Insert succeeded";
    }
    @PostMapping("update/{key}/{value}")
    public String update(@PathVariable("key")String key,@PathVariable("value")String value){
        redisService.upDate(key,value);
        return "Update succeeded";
    }
    @DeleteMapping("delete/{key}")
    public String delete(@PathVariable("key")String key) {
        redisService.delete(key);
        return "Delete succeeded";
    }


}
