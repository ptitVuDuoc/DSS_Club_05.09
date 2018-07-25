package com.dss.dssClub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListGift {

    @SerializedName("LinkAnh")
    @Expose
    private String linkAnh;
    @SerializedName("MaQuaTang")
    @Expose
    private String maQuaTang;
    @SerializedName("QuaTangID")
    @Expose
    private String quaTangID;
    @SerializedName("SoDiemDoi")
    @Expose
    private Integer soDiemDoi;
    @SerializedName("TenQuaTang")
    @Expose
    private String tenQuaTang;

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