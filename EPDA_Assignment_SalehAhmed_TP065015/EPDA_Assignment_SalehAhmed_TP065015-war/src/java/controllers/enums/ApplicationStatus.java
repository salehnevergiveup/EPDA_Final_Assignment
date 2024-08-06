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
public enum ApplicationStatus {
    APPROVED("Approved"),
    PENDING("Pending"),
    REJECTED("Rejected"),
    DELETED("Deleted"),
    INTERVIEW("Interview");

    private final String status;

    ApplicationStatus(String status) {
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