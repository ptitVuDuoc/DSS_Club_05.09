package com.dss.dssClub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListGiftRegistered {

    @SerializedName("MaQuaTang")
    @Expose
    private String maQuaTang;
    @SerializedName("NgayDangKy")
    @Expose
    private String ngayDangKy;
    @SerializedName("RowNumber")
    @Expose
    private Integer rowNumber;
    @SerializedName("SoLuong")
    @Expose
    private int soLuong;
    @SerializedName("TenQuaTang")
    @Expose
    private String tenQuaTang;
    @SerializedName("TinhTrang")
    @Expose
    private String tinhTrang;

    public String getMaQuaTang() {
        return maQuaTang;
    }

    public void setMaQuaTang(String maQuaTang) {
        this.maQuaTang = maQuaTang;
    }

    public String getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(String ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTenQuaTang() {
        return tenQuaTang;
    }

    public void setTenQuaTang(String tenQuaTang) {
        this.tenQuaTang = tenQuaTang;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

}