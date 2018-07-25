package com.dss.dssClub.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListRegisterSeria implements Serializable{

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ListResultSeria")
    @Expose
    private List<ListResultSerium> listResultSeria = null;

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

    public List<ListResultSerium> getListResultSeria() {
        return listResultSeria;
    }

    public void setListResultSeria(List<ListResultSerium> listResultSeria) {
        this.listResultSeria = listResultSeria;
    }

}