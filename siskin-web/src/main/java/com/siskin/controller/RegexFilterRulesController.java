package com.siskin.controller;


import com.siskin.dao.RegexFilterRules;
import com.siskin.service.RegexFilterRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Filter")
public class RegexFilterRulesController {

    @Autowired
    private RegexFilterRulesService regexFilterRulesService;

    @RequestMapping("/getAllFilter")
    public List<RegexFilterRules> getFilter(){
        List<RegexFilterRules> filter = regexFilterRulesService.getFilter();
        return filter;
    }

    @RequestMapping("getFilterById/{rfr_id}")
    public RegexFilterRules getFilterById(@PathVariable int rfr_id){
        return regexFilterRulesService.getFilterById(rfr_id);
    }

    @GetMapping("/insertFilter")
    public Integer insertFilter(@RequestBody RegexFilterRules filter){
        return regexFilterRulesService.insertFilter(filter);
    }

    @PutMapping("/updateFilter")
    public Integer updateFilter(@RequestBody RegexFilterRules filter){
        return regexFilterRulesService.updateFilter(filter);
    }

    @GetMapping("/deleteFilter/{rfr_id}")
    public int deleteFilter(@PathVariable int rfr_id){
        return regexFilterRulesService.deleteFilter(rfr_id);
    }


}
