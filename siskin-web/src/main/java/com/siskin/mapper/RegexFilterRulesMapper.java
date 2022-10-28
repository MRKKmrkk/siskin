package com.siskin.mapper;


import com.siskin.dao.RegexFilterRules;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RegexFilterRulesMapper {
    //查询所有Filter
    List<RegexFilterRules> getFilter();
    //根据id查Filter
    RegexFilterRules getFilterById(int rfr_id);
    //添加数据Filter
    Integer setFilter(RegexFilterRules filter);
    //修改数据Filter
    Integer updateFilter(RegexFilterRules filter);
    //删除数据Filter
    int deleteFilter(int rfr_id);

}
