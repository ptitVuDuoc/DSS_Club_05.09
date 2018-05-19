package com.example.admin.dss_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListSeriaRegistered {

    @SerializedName("MaHangHoa")
    @Expose
    private String maHangHoa;
    @SerializedName("MaSoDuThuong")
    @Expose
    private String maSoDuThuong;
    @SerializedName("NgayDangKy")
    @Expose
    private String ngayDangKy;
    @SerializedName("RowNumber")
    @Expose
    private Integer rowNumber;
    @SerializedName("Seria")
    @Expose
    private String seria;
    @SerializedName("SoDiem")
    @Expose
    private Integer soDiem;
    @SerializedName("TenHangHoa")
    @Expose
    private String tenHangHoa;

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

    public String getTenHangHoa() {
        return tenHangHoa;
    }

    public void setTenHangHoa(String tenHangHoa) {
        this.tenHangHoa = tenHangHoa;
    }

}