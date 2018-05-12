package com.example.admin.dss_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SeriInfo implements Serializable{

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("MaHangHoa")
    @Expose
    private String maHangHoa;
    @SerializedName("MaSoDuThuong")
    @Expose
    private String maSoDuThuong;
    @SerializedName("Seria")
    @Expose
    private String seria;
    @SerializedName("SoDiem")
    @Expose
    private Integer soDiem;
    @SerializedName("SoDiemHienTai")
    @Expose
    private Integer soDiemHienTai;

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

    public String getMaHangHoa() {
        return maHangHoa;
    }

    public void setMaHangHoa(String maHangHoa) {
        this.maHangHoa = maHangHoa;
    }

    public String getMaSoDuThuong() {
        return maSoDuThuong;
    }

    public void setMaSoDuThuong(String maSoDuThuong) {
        this.maSoDuThuong = maSoDuThuong;
    }

    public String getSeria() {
        return seria;
    }

    public void setSeria(String seria) {
        this.seria = seria;
    }

    public Integer getSoDiem() {
        return soDiem;
    }

    public void setSoDiem(Integer soDiem) {
        this.soDiem = soDiem;
    }

    public Integer getSoDiemHienTai() {
        return soDiemHienTai;
    }

    public void setSoDiemHienTai(Integer soDiemHienTai) {
        this.soDiemHienTai = soDiemHienTai;
    }

}