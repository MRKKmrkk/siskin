package com.siskin.service;

import com.alibaba.fastjson.util.IOUtils;
import com.siskin.dao.SpiderIdentifiedRules;
import com.siskin.mapper.SpiderIdentifiedRulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpiderIdentifiedRulesService {

    @Autowired
    private SpiderIdentifiedRulesMapper spiderIdentifiedRulesMapper;

    public List<SpiderIdentifiedRules> getSpider(){
        return spiderIdentifiedRulesMapper.getSpider();
    }

    public SpiderIdentifiedRules getSpiderById(int sir_id){
        return spiderIdentifiedRulesMapper.getSpiderById(sir_id);
    }

    public Integer insertSpider(SpiderIdentifiedRules spider){
        return spiderIdentifiedRulesMapper.setSpider(spider);
    }

    public Integer updateSpider(SpiderIdentifiedRules spider){
        return spiderIdentifiedRulesMapper.updateSpider(spider);
    }

    public int deleteSpider(int sir_id){
        return spiderIdentifiedRulesMapper.deleteSpider(sir_id);
    }

}
