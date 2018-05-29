package com.example.admin.dss_project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetListGiftRegisterd {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ListGiftRegistered")
    @Expose
    private List<ListGiftRegistered> listGiftRegistered = null;
    @SerializedName("PageCount")
    @Expose
    private Integer pageCount;

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

    public List<ListGiftRegistered> getListGiftRegistered() {
        return listGiftRegistered;
    }

    public void setListGiftRegistered(List<ListGiftRegistered> listGiftRegistered) {
        this.listGiftRegistered = listGiftRegistered;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

}