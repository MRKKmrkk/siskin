package com.siskin.service;

import com.siskin.dao.RegexFilterRules;
import com.siskin.mapper.RegexFilterRulesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegexFilterRulesService {

    @Autowired
    private RegexFilterRulesMapper regexFilterRulesMapper;

    public List<RegexFilterRules> getFilter(){
        return regexFilterRulesMapper.getFilter();
    }

    public RegexFilterRules getFilterById(int rfr_id){
        return regexFilterRulesMapper.getFilterById(rfr_id);
    }

    public Integer insertFilter(RegexFilterRules filter){
        return regexFilterRulesMapper.setFilter(filter);
    }

    public Integer updateFilter(RegexFilterRules filter) {
        return regexFilterRulesMapper.updateFilter(filter);
    }

    public int deleteFilter(int rfr_id){
        return regexFilterRulesMapper.deleteFilter(rfr_id);
    }

}
