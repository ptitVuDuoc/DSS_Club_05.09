
package com.example.admin.dss_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DaXacThuc")
    @Expose
    private Boolean daXacThuc;
    @SerializedName("DiaChi")
    @Expose
    private String diaChi;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("HoVaTen")
    @Expose
    private String hoVaTen;
    @SerializedName("MaXacThuc")
    @Expose
    private String maXacThuc;
    @SerializedName("NgaySinh")
    @Expose
    private String ngaySinh;
    @SerializedName("SoDiemHienTai")
    @Expose
    private Integer soDiemHienTai;
    @SerializedName("SoDienThoai")
    @Expose
    private String soDienThoai;
    @SerializedName("TenDaiLy")
    @Expose
    private String tenDaiLy;
    @SerializedName("UserID")
    @Expose
    private String userID;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public Boolean getDaXacThuc() {
        return daXacThuc;
    }

    public void setDaXacThuc(Boolean daXacThuc) {
        this.daXacThuc = daXacThuc;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getMaXacThuc() {
        return maXacThuc;
    }

    public void setMaXacThuc(String maXacThuc) {
        this.maXacThuc = maXacThuc;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
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

    public String getTenDaiLy() {
        return tenDaiLy;
    }

    public void setTenDaiLy(String tenDaiLy) {
        this.tenDaiLy = tenDaiLy;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}