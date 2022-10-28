package com.siskin.controller;

import com.siskin.dao.SpiderIdentifiedRules;
import com.siskin.service.SpiderIdentifiedRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Spider")
public class SpiderIdentifiedRulesController {

    @Autowired
    private SpiderIdentifiedRulesService spiderIdentifiedRulesService;

    @RequestMapping("/getAllSpider")
    public List<SpiderIdentifiedRules> getSpider(){
        return spiderIdentifiedRulesService.getSpider();
    }

    @RequestMapping("getSpiderById/{sir_id}")
    public SpiderIdentifiedRules getSpiderById(@PathVariable int sir_id){
        return spiderIdentifiedRulesService.getSpiderById(sir_id);
    }

    @PostMapping("/insertSpider")
    public Integer insertSpider(@RequestBody SpiderIdentifiedRules spider){
        return spiderIdentifiedRulesService.insertSpider(spider);
    }

    @PutMapping("/updateSpider")
    public Integer updateSpider(@RequestBody SpiderIdentifiedRules spider){
        return spiderIdentifiedRulesService.updateSpider(spider);
    }

    @DeleteMapping("/deletespider/{sir_id}")
    public int deleteSpider(@PathVariable int sir_id){
        return spiderIdentifiedRulesService.deleteSpider(sir_id);
    }
}
