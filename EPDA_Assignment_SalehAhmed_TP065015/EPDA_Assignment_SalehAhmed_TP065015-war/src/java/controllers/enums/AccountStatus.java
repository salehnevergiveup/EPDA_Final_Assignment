/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.enums;

/**
 *
 * @author saleh
 */
public enum AccountStatus {
    PENDING("Pending"),
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    REJECTED("Rejected");

    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return status;
    }
}
