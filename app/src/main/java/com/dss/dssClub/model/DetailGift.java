package com.dss.dssClub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailGift {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("LinkAnh")
    @Expose
    private String linkAnh;
    @SerializedName("MaQuaTang")
    @Expose
    private String maQuaTang;
    @SerializedName("MoTa")
    @Expose
    private String moTa;
    @SerializedName("QuaTangID")
    @Expose
    private String quaTangID;
    @SerializedName("SoDiemDoi")
    @Expose
    private Integer soDiemDoi;
    @SerializedName("TenQuaTang")
    @Expose
    private String tenQuaTang;

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

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getMaQuaTang() {
        return maQuaTang;
    }

    public void setMaQuaTang(String maQuaTang) {
        this.maQuaTang = maQuaTang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getQuaTangID() {
        return quaTangID;
    }

    public void setQuaTangID(String quaTangID) {
        this.quaTangID = quaTangID;
    }

    public Integer getSoDiemDoi() {
        return soDiemDoi;
    }

    public void setSoDiemDoi(Integer soDiemDoi) {
        this.soDiemDoi = soDiemDoi;
    }

    public String getTenQuaTang() {
        return tenQuaTang;
    }

    public void setTenQuaTang(String tenQuaTang) {
        this.tenQuaTang = tenQuaTang;
    }

}