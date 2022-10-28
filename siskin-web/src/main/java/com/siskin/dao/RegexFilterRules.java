package com.siskin.dao;


public class RegexFilterRules {

    //Filter
    private long rfr_id;
    private String field_name;
    private String regex_rule;

    public long getRfr_id() {
        return rfr_id;
    }

    public void setRfr_id(int rfr_id) {
        this.rfr_id = rfr_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getRegex_rule() {
        return regex_rule;
    }

    public void setRegex_rule(String regex_rule) {
        this.regex_rule = regex_rule;
    }

    @Override
    public String toString() {
        return "RegexFilterRules{" +
                "rfr_id=" + rfr_id +
                ", field_name='" + field_name + '\'' +
                ", regex_rule='" + regex_rule + '\'' +
                '}';
    }
}
