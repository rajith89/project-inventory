package com.ictasl.java.projectinventory.Web.dto;

public enum Status {

    Pending(0), Recommended(1), Reviewing(2);
    private Integer statusVal;

    Status(Integer status){
        this.statusVal = status;
    }

    public Integer getStatusVal(){
        return this.statusVal;
    }
}

