package com.example.admin.dss_project.model;

import java.util.List;

public class PostRawRegiterSeria {
    private String TokenKey;
    private String SoDienThoai;
    private List<String> ArraySeria;

    public PostRawRegiterSeria() {
    }

    public List<String> getArraySeria() {
        return ArraySeria;
    }

    public void setArraySeria(List<String> arraySeria) {
        ArraySeria = arraySeria;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getTokenKey() {
        return TokenKey;
    }

    public void setTokenKey(String tokenKey) {
        TokenKey = tokenKey;
    }

    public PostRawRegiterSeria(String tokenKey, String soDienThoai,List<String> arraySeria ) {

        TokenKey = tokenKey;
        SoDienThoai = soDienThoai;
        ArraySeria = arraySeria;
    }
}
