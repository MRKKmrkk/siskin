package com.siskin.dao;

public class RegexEncryptRules {
    //Encrypt
    private int rer_id;
    private String field_name;
    private String encrypt_method;

    public int getRer_id() {
        return rer_id;
    }

    public void setRer_id(int rer_id) {
        this.rer_id = rer_id;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getEncrypt_method() {
        return encrypt_method;
    }

    public void setEncrypt_method(String encrypt_method) {
        this.encrypt_method = encrypt_method;
    }

    @Override
    public String toString() {
        return "RegexEncryptRules{" +
                "rer_id=" + rer_id +
                ", field_name='" + field_name + '\'' +
                ", encrypt_method='" + encrypt_method + '\'' +
                '}';
    }
}
