package com.siskin.dao;

public class SpiderIdentifiedRules {
    //Spider
    private int sir_id;
    private String identified_rules;
    private String start_time;
    private String end_time;

    public int getSir_id() {
        return sir_id;
    }

    public void setSir_id(int sir_id) {
        this.sir_id = sir_id;
    }

    public String getIdentified_rules() {
        return identified_rules;
    }

    public void setIdentified_rules(String identified_rules) {
        this.identified_rules = identified_rules;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "SpiderIdentifiedRules{" +
                "sir_id=" + sir_id +
                ", identified_rules='" + identified_rules + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}
