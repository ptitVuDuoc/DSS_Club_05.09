package com.dss.dssClub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetScoresAcc {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("SoDiemHienTai")
    @Expose
    private Integer soDiemHienTai;
    @SerializedName("SoDienThoai")
    @Expose
    private String soDienThoai;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSoDiemHienTai() {
        return soDiemHienTai;
    }

    public void setSoDiemHienTai(Integer soDiemHienTai) {
        this.soDiemHienTai = soDiemHienTai;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

}