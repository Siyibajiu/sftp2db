package com.adidas.sftp.dto;

public class MemberCrm {

    private String userId;
    private String username;
    private String phonenumber;

    public MemberCrm() {
    }

    public MemberCrm(String userId, String phonenumber, String username) {
        this.userId = userId;
        this.phonenumber = phonenumber;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
