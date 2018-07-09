package com.example.admin.dss_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notifi implements Serializable{

    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("LinkWeb")
    @Expose
    private String linkWeb;
    @SerializedName("Ngay")
    @Expose
    private String ngay;
    @SerializedName("NoiDung")
    @Expose
    private String noiDung;
    @SerializedName("RowNumber")
    @Expose
    private Integer rowNumber;
    @SerializedName("TieuDe")
    @Expose
    private String tieuDe;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

}