package com.example.admin.dss_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetListRegitedSeria {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("ListSeriaRegistered")
    @Expose
    private ArrayList<ListSeriaRegistered> listSeriaRegistered = null;
    @SerializedName("PageCount")
    @Expose
    private Integer pageCount;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public ArrayList<ListSeriaRegistered> getListSeriaRegistered() {
        return listSeriaRegistered;
    }

    public void setListSeriaRegistered(ArrayList<ListSeriaRegistered> listSeriaRegistered) {
        this.listSeriaRegistered = listSeriaRegistered;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

}