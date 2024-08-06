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
public enum ReportType {
    COMMENT_FEEDBACK_ANALYSIS("Comment and Feedback Analysis Report"),
    JOBS("Jobs"),
    APPLICATIONS("Applications"),
    JOBSEEKERS("Jobseekers"),
    CUSTOMERS("Customers"),
    WARNINGS("Warnings"),
    GenderAndAge("Gender&Age");

    private final String type;

    ReportType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
