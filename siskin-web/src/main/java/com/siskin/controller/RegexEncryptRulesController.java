package com.siskin.controller;

import com.siskin.dao.RegexEncryptRules;
import com.siskin.service.RegexEncryptRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Encrypt")
public class RegexEncryptRulesController {

    @Autowired
    private RegexEncryptRulesService regexEncryptRulesService;

    @RequestMapping("/getAllEncrypt")
    public List<RegexEncryptRules> getEncrypt(){
        return regexEncryptRulesService.getEncrypt();
    }

    @RequestMapping("getEncryptById/{rer_id}")
    public RegexEncryptRules getEncryptById(@PathVariable int rer_id){
        return regexEncryptRulesService.getEncryptById(rer_id);
    }

    @PostMapping("/insertEncrypt")
    public Integer insertEncrypt(@RequestBody RegexEncryptRules encrypt){
        return regexEncryptRulesService.insertEncrypt(encrypt);
    }

    @PutMapping("/updateEncrypt")
    public Integer updateEncrypt(@RequestBody RegexEncryptRules encrypt){
        return regexEncryptRulesService.updateEncrypt(encrypt);
    }

    @DeleteMapping("/deleteEncrypt/{rer_id}")
    public int deleteEncrypt(@PathVariable int rer_id){
        return regexEncryptRulesService.deleteEncrypt(rer_id);
    }
}
