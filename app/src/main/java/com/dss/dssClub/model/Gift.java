package com.dss.dssClub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Gift {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("ListGift")
    @Expose
    private List<ListGift> listGift = null;
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

    public List<ListGift> getListGift() {
        return listGift;
    }

    public void setArrayListGift(ArrayList<ListGift> ListGift) {
        this.listGift = ListGift;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

}