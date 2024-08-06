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
public enum JspPackage {
    APPLICATIONS("applications"),
    COMMENTS("comments"),
    ERRORS("errors"),
    FEEDBACKS("feedbacks"),
    INCLUDES("includes"),
    JOBS("jobs"),
    PROFILE("profile"),
    USERS("users"),
    REPORTS("reports"),
    WARNINGS("warnings");

    private final String path;

    JspPackage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
