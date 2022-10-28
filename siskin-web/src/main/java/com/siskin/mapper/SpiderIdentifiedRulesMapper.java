package com.siskin.mapper;


import com.siskin.dao.SpiderIdentifiedRules;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SpiderIdentifiedRulesMapper {
    //查询所有spider
    List<SpiderIdentifiedRules> getSpider();
    //根据id查询spider
    SpiderIdentifiedRules getSpiderById(int sir_id);
    //添加数据
    Integer setSpider(SpiderIdentifiedRules spider);
    //修改数据
    Integer updateSpider(SpiderIdentifiedRules spider);
    //删除数据
    int deleteSpider(int sir_id);
}
