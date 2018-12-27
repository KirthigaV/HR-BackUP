package com.homedepot.hr.hr.retailstaffing.dto.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class UserProfileResponseDTO {
    @XStreamAlias("userId")
    private String userId;
    @XStreamAlias("isValidRole")
    private boolean isValidRole;
    @XStreamAlias("firstName")
    private String firstName;
    @XStreamAlias("lastName")
    private String lastName;
    
    @XStreamAlias("strNbr")
    private String strNbr;
        
    
    @XStreamAlias("Status")
    protected String status;
    @XStreamAlias("ErrorCd")
    protected int errorCd;
    @XStreamAlias("ErrorDesc")
    protected String errorDesc;
    
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean isValidRole() {
        return isValidRole;
    }
    public void setValidRole(boolean isValidRole) {
        this.isValidRole = isValidRole;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getErrorCd() {
        return errorCd;
    }
    public void setErrorCd(int errorCd) {
        this.errorCd = errorCd;
    }
    public String getErrorDesc() {
        return errorDesc;
    }
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
    public String getStrNbr() {
        return strNbr;
    }
    public void setStrNbr(String strNbr) {
        this.strNbr = strNbr;
    }
    
    
}
